package com.example.model;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;


@Component
public class OrderShippingData {
    private List<Long> selectedOrderIds;

    // getters and setters
    public List<Long> getSelectedOrderIds() {
        return selectedOrderIds;
    }

    public void setSelectedOrderIds(List<Long> selectedOrderIds) {
        this.selectedOrderIds = selectedOrderIds;
    }
}
