package com.example.controller;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

import org.apache.groovy.util.Arrays;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.constants.Message;
import com.example.enums.OrderStatus;
import com.example.enums.PaymentMethod;
import com.example.enums.PaymentStatus;
import com.example.form.OrderForm;
import com.example.model.Order;
import com.example.model.OrderDeliveries;
import com.example.model.OrderShipping;
import com.example.model.OrderShippingData;
import com.example.service.OrderDeliveriesService;
import com.example.service.OrderService;
import com.example.service.OrderShippingService;
import com.example.service.ProductService;

import groovy.transform.AutoExternalize;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderShippingService orderShippingService;

	@Autowired
	private OrderDeliveriesService orderDeliveriesService;
	

    @GetMapping
    public String index(Model model) {
        List<Order> all = orderService.findAll();
        model.addAttribute("orders", all);
        return "order/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") Long id) {
        if (id != null) {
            Optional<Order> order = orderService.findOne(id);
            model.addAttribute("order", order.get());
        }
        return "order/show";
    }

    @GetMapping(value = "/new")
    public String create(Model model, @ModelAttribute OrderForm.Create entity) {
        model.addAttribute("order", entity);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("paymentMethods", PaymentMethod.values());
        return "order/create";
    }

    @PostMapping
    public String create(@Validated @ModelAttribute OrderForm.Create entity, BindingResult result,
                         RedirectAttributes redirectAttributes) {
        Order order = null;
        try {
            order = orderService.create(entity);
            redirectAttributes.addFlashAttribute("success", Message.MSG_SUCESS_INSERT);
            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", Message.MSG_ERROR);
            e.printStackTrace();
            return "redirect:/orders";
        }
    }

    @GetMapping("/{id}/edit")
    public String update(Model model, @PathVariable("id") Long id) {
        try {
            if (id != null) {
                Optional<Order> entity = orderService.findOne(id);
                model.addAttribute("order", entity.get());
                model.addAttribute("paymentMethods", PaymentMethod.values());
                model.addAttribute("paymentStatus", PaymentStatus.values());
                model.addAttribute("orderStatus", OrderStatus.values());
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return "order/form";
    }

    @PutMapping
    public String update(@Validated @ModelAttribute Order entity, BindingResult result,
                         RedirectAttributes redirectAttributes) {
        Order order = null;
        try {
            order = orderService.save(entity);
            redirectAttributes.addFlashAttribute("success", Message.MSG_SUCESS_UPDATE);
            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", Message.MSG_ERROR);
            e.printStackTrace();
            return "redirect:/orders";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            if (id != null) {
                Optional<Order> entity = orderService.findOne(id);
                orderService.delete(entity.get());
                redirectAttributes.addFlashAttribute("success", Message.MSG_SUCESS_DELETE);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", Message.MSG_ERROR);
            throw new ServiceException(e.getMessage());
        }
        return "redirect:/orders";
    }

    @PostMapping("/{id}/payments")
    public String createPayment(@Validated @ModelAttribute OrderForm.CreatePayment entity, BindingResult result,
                                RedirectAttributes redirectAttributes) {
        try {
            orderService.createPayment(entity);
            redirectAttributes.addFlashAttribute("success", Message.MSG_SUCESS_PAYMENT_INSERT);
            return "redirect:/orders/" + entity.getOrderId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", Message.MSG_ERROR);
            e.printStackTrace();
            return "redirect:/orders";
        }
    }

    @GetMapping("/shipping")
    public String shipping(Model model) {
        List<Order> orders = orderService.findByStatus("ordered");
        
        model.addAttribute("orders", orders);
        return "order/shipping";
    }

    @PostMapping("/shipping/download")
    public void download(HttpServletResponse response) {
        String csvHeader = "orderId,shippingCode,shippingDate,deliveryDate,deliveryTimezone\n";
        StringBuilder csvBuilder = new StringBuilder(csvHeader);
        List<Order> orders = this.orderService.findByStatus("ordered");

        for (Order order : orders) {
            csvBuilder.append(order.getId()).append(",");
            csvBuilder.append(",");
            csvBuilder.append(",");
            csvBuilder.append(",");
            csvBuilder.append(",").append("\n");
        }

        try {
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"shipping.csv\"");
    
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(csvBuilder.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/shipping")
    public String uploadFile(@RequestParam("file") MultipartFile uploadFile, RedirectAttributes redirectAttributes, Model model) {
        if (uploadFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("validationError", "ファイルを選択してください。");
            return "redirect:/orders/shipping";
        }

        if (!"text/csv".equals(uploadFile.getContentType())) {
            redirectAttributes.addFlashAttribute("validationError", "CSVファイルを選択してください。");
            return "redirect:/orders/shipping";
        }

        try {

			List<OrderDeliveries> deliveries = orderService.importCSV(uploadFile);
			System.out.println(deliveries);
            model.addAttribute("orderShippingList", deliveries);

            return "order/shipping";
        } catch (Throwable e) {
            redirectAttributes.addFlashAttribute("validationError", e.getMessage());
            e.printStackTrace();
            return "redirect:/orders/shipping";
        }
    }

    @PutMapping("/shipping/update")
    public String updateShippingStatus(@ModelAttribute OrderShippingData orderShippingData) {
        List<Long> selectedOrderIds = orderShippingData.getSelectedOrderIds();
		orderService.updateOrderStatusToShipped(selectedOrderIds);
        return "redirect:/orders/shipping";
    }
}
