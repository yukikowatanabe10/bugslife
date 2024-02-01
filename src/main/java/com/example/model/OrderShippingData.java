package com.example.model;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;


@Component
public class OrderShippingData {
    private List<OrderShipping> orderShippingList;
    

    public OrderShippingData() {}

    public OrderShippingData(List<OrderShipping> orderShippingList) {
        this.orderShippingList = orderShippingList;
    }

    public void setOrderShippingList(List<OrderShipping> orderShippingList) {
        this.orderShippingList = orderShippingList;
    }

    public List<OrderShipping> getOrderShippingList() {
        return orderShippingList;
    }

    public List<OrderShipping> getSelectedOrders() {
        List<OrderShipping> selectedOrders = new ArrayList<>();
        for (OrderShipping orderShipping : orderShippingList) {
            if ("1".equals(orderShipping.getUploadStatus())) {
                selectedOrders.add(orderShipping);
            }
        }
        return selectedOrders;
    }
}
