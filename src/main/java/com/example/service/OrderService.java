package com.example.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.antlr.v4.runtime.atn.SemanticContext.OR;
import org.codehaus.groovy.syntax.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.nio.charset.StandardCharsets;

import com.example.constants.TaxType;
import com.example.enums.CampaignStatus;
import com.example.enums.DiscountType;
import com.example.enums.OrderStatus;
import com.example.enums.PaymentStatus;
import com.example.form.OrderForm;
import com.example.model.Campaign;
import com.example.model.Order;
import com.example.model.OrderDeliveries;
import com.example.model.OrderPayment;
import com.example.model.OrderProduct;
import com.example.model.OrderShipping;
import com.example.model.OrderShippingData;
import com.example.repository.OrderDeliveriesRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDeliveriesRepository orderDeliveries;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<OrderDeliveries> findAllOrderDeliveries() {
        return orderDeliveries.findAll();
    }

    public Optional<Order> findOne(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional(readOnly = false)
    public Order save(Order entity) {
        return orderRepository.save(entity);
    }

    @Transactional(readOnly = false)
    public Order create(OrderForm.Create entity) {
        Order order = new Order();
        order.setCustomerId(entity.getCustomerId());
        order.setShipping(entity.getShipping());
        order.setNote(entity.getNote());
        order.setPaymentMethod(entity.getPaymentMethod());
        order.setStatus(OrderStatus.ORDERED);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setPaid(0.0);

        var orderProducts = new ArrayList<OrderProduct>();
        entity.getOrderProducts().forEach(p -> {
            var product = productRepository.findById(p.getProductId()).get();
            var orderProduct = new OrderProduct();
            orderProduct.setProductId(product.getId());
            orderProduct.setCode(product.getCode());
            orderProduct.setName(product.getName());
            orderProduct.setQuantity(p.getQuantity());
            orderProduct.setPrice((double) product.getPrice());
            orderProduct.setDiscount(p.getDiscount());
            orderProduct.setTaxType(TaxType.get(product.getTaxType()));
            orderProducts.add(orderProduct);
        });

        var total = 0.0;
        var totalTax = 0.0;
        var totalDiscount = 0.0;
        for (var orderProduct : orderProducts) {
            var price = orderProduct.getPrice();
            var quantity = orderProduct.getQuantity();
            var discount = orderProduct.getDiscount();
            var tax = 0.0;
            if (orderProduct.getTaxIncluded()) {
                tax = price * quantity * orderProduct.getTaxRate() / (100 + orderProduct.getTaxRate());
            } else {
                tax = price * quantity * orderProduct.getTaxRate() / 100;
            }
            tax = switch (orderProduct.getTaxRounding()) {
                case TaxType.ROUND -> Math.round(tax);
                case TaxType.CEIL -> Math.ceil(tax);
                case TaxType.FLOOR -> Math.floor(tax);
                default -> tax;
            };
            var subTotal = price * quantity + tax - discount;
            total += subTotal;
            totalTax += tax;
            totalDiscount += discount;
        }
        order.setTotal(total);
        order.setTax(totalTax);
        order.setDiscount(totalDiscount);
        order.setGrandTotal(total + order.getShipping());
        order.setOrderProducts(orderProducts);

        orderRepository.save(order);

        return order;
    }

    @Transactional
    public void delete(Order entity) {
        orderRepository.delete(entity);
    }

    @Transactional(readOnly = false)
    public void createPayment(OrderForm.CreatePayment entity) {
        var order = orderRepository.findById(entity.getOrderId()).get();
        var payment = new OrderPayment();
        payment.setType(entity.getType());
        payment.setPaid(entity.getPaid());
        payment.setMethod(entity.getMethod());
        payment.setPaidAt(entity.getPaidAt());

        order.getOrderPayments().add(payment);
        var paid = order.getOrderPayments().stream().mapToDouble(p -> p.getPaid()).sum();
        var paymentStatus = paid > order.getGrandTotal() ? PaymentStatus.OVERPAID
                : paid < order.getGrandTotal() ? PaymentStatus.PARTIALLY_PAID : PaymentStatus.PAID;

        order.setPaid(paid);
        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
    }

    public Order findOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }

    @Transactional
    public List<Order> findByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Transactional
    public List<OrderDeliveries> importCSV(MultipartFile file) throws IOException {
        List<OrderDeliveries> deliveries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                final String[] split = line.replace("\"", "").split(",");
                Long id = Long.parseLong(split[0]);
                String shippingCode = split[1];
                LocalDate shippingDateLocal = LocalDate.parse(split[2]);
                Timestamp shippingDate = Timestamp.valueOf(shippingDateLocal.atStartOfDay());
                LocalDate deliveryDateLocal = LocalDate.parse(split[3]);
                Timestamp deliveryDate = Timestamp.valueOf(deliveryDateLocal.atStartOfDay());
                String deliveryTimeZone = split[4];
                Order order = findOrderById(Long.parseLong(split[0]));
                if (order == null) {
                    continue; // Skip if Order not found
                }
                OrderDeliveries orderDelivery = new OrderDeliveries(id, shippingCode, shippingDate, deliveryDate, deliveryTimeZone);
                orderDelivery.setOrder(order);
                deliveries.add(orderDelivery);
            }
        }
        return deliveries;
    }

	@Transactional
	public void updateOrderStatusToShipped(List<Long> orderIds){
		List<Order> orders = orderRepository.findAllById(orderIds);
		for (Order order : orders) {
            if ("ordered".equals(order.getStatus())) {
                order.setStatus("shipped");
                orderRepository.save(order);
            }
        }
	}
}
