package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.form.TaxAmountForm;
import com.example.model.Product;
import com.example.model.TaxAmount;
import com.example.repository.TaxAmountRepository;

@Service
@Transactional(readOnly = true)
public class TaxAmountService {

	@Autowired
	private TaxAmountRepository taxAmountRepository;

	public List<TaxAmount> findAll() {
		return taxAmountRepository.findAll();
	}

	@Transactional(readOnly = false)
	public TaxAmount save(TaxAmountForm taxAmountForm) {
		TaxAmount taxAmount = new TaxAmount();
		taxAmount.setRate(taxAmountForm.getRate());
		return this.taxAmountRepository.save(taxAmount);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		taxAmountRepository.deleteById(id);
	}

	@Transactional(readOnly = false)
	public Optional<TaxAmount> findById(Long id) {
		return this.taxAmountRepository.findById(id);
	}
}
