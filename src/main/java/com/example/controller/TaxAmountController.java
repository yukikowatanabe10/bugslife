package com.example.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.form.TaxAmountForm;
import com.example.model.TaxAmount;
import com.example.service.TaxAmountService;

@Controller
@RequestMapping("/taxAmount")
public class TaxAmountController {

	@Autowired
	private TaxAmountService taxamountService;

	@GetMapping
	public String index(Model model) {
		List<TaxAmount> taxAmounts = taxamountService.findAll();
		Set<Double> uniqueRates = new HashSet<>();
		List<TaxAmount> uniqueTaxAmounts = new ArrayList<>();
		for (TaxAmount taxAmount : taxAmounts) {
			Double rate = taxAmount.getRate();
			if (!uniqueRates.contains(rate)) {
				uniqueRates.add(rate);
				uniqueTaxAmounts.add(taxAmount);
			}
		}
		model.addAttribute("taxAmounts", uniqueTaxAmounts);
		return "taxAmount/index";
	}

	@GetMapping(value = "/new")
	public String create(Model model) {
		model.addAttribute("taxAmount", new TaxAmountForm());
		return "taxAmount/form";
	}

	@PostMapping
	public String create(@ModelAttribute TaxAmountForm taxAmountForm, TaxAmount taxAmount) {
		this.taxamountService.save(taxAmountForm, taxAmount);
		return "redirect:/taxAmount";
	}

	@GetMapping("/show/{id}")
	public String show(@PathVariable("id") Long id, Model model) {
		Optional<TaxAmount> taxAmount = this.taxamountService.findById(id);
		if (taxAmount.isPresent()) {
			model.addAttribute("taxAmount", taxAmount.get());
			return "taxAmount/show";
		} else {
			return "redirect:/taxAmount";
		}
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") Long id) {
		Optional<TaxAmount> taxAmount = taxamountService.findById(id);
		if (taxAmount.isPresent()) {
			taxamountService.deleteByRate(taxAmount.get());
		}
		return "redirect:/taxAmount";
	}

}