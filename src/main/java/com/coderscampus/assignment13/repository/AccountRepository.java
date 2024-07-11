package com.coderscampus.assignment13.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

	

	void save(User user);

	Account findByAccountName(String accountName);
	
}
