package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.TaxAmount;

public interface TaxAmountRepository extends JpaRepository<TaxAmount, Long> {

}
