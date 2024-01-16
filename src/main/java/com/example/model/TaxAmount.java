package com.example.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "taxAmount")
public class TaxAmount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "rate", nullable = false)
	private double rate;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "taxincome")
	private boolean taxincome;

	@Column(name = "floor")
	private boolean floor;

	@Column(name = "round")
	private boolean round;

	@Column(name = "ceil")
	private boolean ceil;

	public double getRate() {
		return this.rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getTaxincome() {
		return taxincome;
	}

	public void setTaxincome(boolean taxincome) {
		this.taxincome = taxincome;
	}

	public boolean getFloor() {
		return this.floor;
	}

	public void setFloor(boolean floor) {
		this.floor = floor;
	}

	public boolean getRound() {
		return this.round;
	}

	public void setRound(boolean round) {
		this.round = round;
	}

	public boolean getCeil() {
		return this.ceil;
	}

	public void setCeil(boolean ceil) {
		this.ceil = ceil;
	}

}
