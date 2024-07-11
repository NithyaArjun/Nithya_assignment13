package com.coderscampus.assignment13.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AddressRepository;
import com.coderscampus.assignment13.repository.UserRepository;

@Service

public class AddressService {
	@Autowired
	private AddressRepository addressRepo;
	@Autowired
	private UserRepository userRepo;

	public Address findByUserId(Long userId) {
		return addressRepo.findByUserId(userId);
	}

	public Address saveAddress(Address address) {
		return addressRepo.save(address);
	}

	@Transactional
	public User saveOrUpdateAddress(Long userId, String addressLine1, String addressLine2, String city, String region,
			String country, String zipCode) {

		User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		Address address = user.getAddress();
		if (address == null) {
			address = new Address();
			address.setUser(user);
			user.setAddress(address);
		}

		address.setAddressLine1(addressLine1);
		address.setAddressLine2(addressLine2);
		address.setCity(city);
		address.setRegion(region);
		address.setCountry(country);
		address.setZipCode(zipCode);
		address.setUser(user);
		addressRepo.save(address);
		user.setAddress(address);
		userRepo.save(user);

		return user;
	}

}
