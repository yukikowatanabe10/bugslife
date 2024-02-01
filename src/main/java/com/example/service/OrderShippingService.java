package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.OrderShipping;
import com.example.repository.OrderShippingRepository;

@Service
public class OrderShippingService {
    @Autowired
    private OrderShippingRepository orderShippingRepository;
    
    public OrderShipping save(OrderShipping orderShipping){
        return this.orderShippingRepository.save(orderShipping);
    }
}