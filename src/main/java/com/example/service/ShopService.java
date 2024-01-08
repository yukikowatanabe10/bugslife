package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Shop;
import com.example.repository.ShopRepository;

@Service
@Transactional(readOnly = true)
public class ShopService {

	@Autowired
	private ShopRepository shopRepository;

	public List<Shop> findAll() {
		return shopRepository.findAll();
	}

	public List<Shop> findAll(Shop probe) {
		return shopRepository.findAll(Example.of(probe));
	}

	public List<Shop> serchShops(String name) {
		return shopRepository.findByNameContaining(name);
	}

	public Optional<Shop> findOne(Long id) {
		return shopRepository.findById(id);
	}

	@Transactional(readOnly = false)
	public Shop save(Shop entity) {
		return shopRepository.save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(Shop entity) {
		shopRepository.delete(entity);
	}

}
