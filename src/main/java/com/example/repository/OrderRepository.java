package com.example.repository;
import java.util.List; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    @Query("SELECT o FROM Order o WHERE o.paymentStatus = ?1 OR o.paymentStatus = ?2")
    List<Order> findByPaymentStatusOrPaymentStatus(String paymentStatus1, String paymentStatus2);
}


