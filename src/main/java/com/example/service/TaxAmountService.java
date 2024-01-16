package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.form.TaxAmountForm;
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
	public void delete(Long id) {
		taxAmountRepository.deleteById(id);
	}

	@Transactional(readOnly = false)
	public Optional<TaxAmount> findById(Long id) {
		return this.taxAmountRepository.findById(id);
	}

	@Transactional(readOnly = false)
	public void save(TaxAmountForm taxAmountForm, TaxAmount taxAmount) {
		List<TaxAmount> existingTaxAmounts = taxAmountRepository.findByRate(taxAmountForm.getRate());
		if (!existingTaxAmounts.isEmpty()) {
			throw new IllegalStateException("登録済みの税率と同じ数値は登録できません。");
		} else {
			for (boolean taxincome : new boolean[] { true, false }) {
				for (boolean[] roundings : new boolean[][] { { true, false, false }, { false, true, false },
						{ false, false, true } }) {
					taxAmount = new TaxAmount();
					taxAmount.setName(taxAmountForm.getName());
					taxAmount.setRate(taxAmountForm.getRate());
					taxAmount.setTaxincome(taxincome);
					taxAmount.setFloor(roundings[0]);
					taxAmount.setRound(roundings[1]);
					taxAmount.setCeil(roundings[2]);
					taxAmountRepository.save(taxAmount);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void deleteByRate(TaxAmount taxAmount) {
		double rate = taxAmount.getRate();
		taxAmountRepository.deleteByRate(rate);
	}
}
