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
            // ここで選択された注文を判定し、selectedOrders に追加するロジックを実装
            // 例えば、uploadStatus が "2" の注文を選択した場合
            if ("2".equals(orderShipping.getUploadStatus())) {
                selectedOrders.add(orderShipping);
            }
        }
        return selectedOrders;
    }
}
