package com.coderscampus.assignment13.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.AddressRepository;
import com.coderscampus.assignment13.repository.UserRepository;

@Service

public class AccountService {
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private UserRepository userRepo;

	public Set<Account> findAll() {
		return new HashSet<>(accountRepo.findAll());
	}

	public void saveAccount(Account account) {
		accountRepo.save(account);

	}

	public void saveAccountById(Long accountId, User user) {
		// TODO Auto-generated method stub

	}

	public void save(Account account) {
		accountRepo.save(account);

	}

	public void saveAll(List<Account> accounts) {
		accountRepo.saveAll(accounts);
	}

	public Account finById(Long accountId) {
		accountRepo.findById(accountId);
		return null;
	}

}
