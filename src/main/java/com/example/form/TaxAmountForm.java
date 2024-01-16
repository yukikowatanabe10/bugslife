package com.example.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TaxAmountForm {

	private String name;
	private double rate;

	public String getName() {
		return this.name;
	}

	public Double getRate() {
		return this.rate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}
}
