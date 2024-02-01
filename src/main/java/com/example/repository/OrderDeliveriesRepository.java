package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.OrderDeliveries;

@Repository
public interface OrderDeliveriesRepository extends JpaRepository<OrderDeliveries,Long>{
    
}
