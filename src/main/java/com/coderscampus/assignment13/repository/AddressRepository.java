package com.coderscampus.assignment13.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;



@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByUserId(Long userId);   

	
}