package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.example.model.OrderDeliveries;
import com.example.model.OrderShippingData;
import com.example.repository.OrderDeliveriesRepository;
import java.sql.Timestamp;
import java.util.ArrayList;



@Service
public class OrderDeliveriesService {
    @Autowired
    private OrderDeliveriesRepository orderDeliveriesRepository;

    
}
