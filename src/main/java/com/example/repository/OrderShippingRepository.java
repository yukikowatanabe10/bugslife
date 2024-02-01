package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.OrderShipping;

@Repository
public interface OrderShippingRepository extends JpaRepository<OrderShipping,Long>{
    
}
