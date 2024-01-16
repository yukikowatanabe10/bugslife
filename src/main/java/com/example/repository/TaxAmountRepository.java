package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.TaxAmount;

public interface TaxAmountRepository extends JpaRepository<TaxAmount, Long> {

	List<TaxAmount> findByRate(double rate);

	void deleteByRate(double rate);
}
