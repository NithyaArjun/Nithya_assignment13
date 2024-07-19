package com.coderscampus.assignment13.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.AddressRepository;
import com.coderscampus.assignment13.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private AddressService addressService;
	@Autowired
	private AddressRepository addressRepo;

	public List<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public List<User> findByNameAndUsername(String name, String username) {
		return userRepo.findByNameAndUsername(name, username);
	}

	public List<User> findByCreatedDateBetween(LocalDate date1, LocalDate date2) {
		return userRepo.findByCreatedDateBetween(date1, date2);
	}

	public User findExactlyOneUserByUsername(String username) {
		List<User> users = userRepo.findExactlyOneUserByUsername(username);
		if (users.size() > 0)
			return users.get(0);
		else
			return new User();
	}

	public Set<User> findAll() {
		return userRepo.findAllUsersWithAccountsAndAddresses();
	}

	public User findById(Long userId) {
		Optional<User> userOpt = userRepo.findById(userId);
		return userOpt.orElse(new User());
	}

	public User saveUser(User user) {
		if (user.getUserId() == null) {
			Account checking = new Account();
			checking.setAccountName("Checking Account");
			checking.getUsers().add(user);
			Account savings = new Account();
			savings.setAccountName("Savings Account");
			savings.getUsers().add(user);

			user.getAccounts().add(checking);
			user.getAccounts().add(savings);
			accountRepo.save(checking);
			accountRepo.save(savings);
		}
		return userRepo.save(user);
	}

	@Transactional
    public void delete(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.delete(user);
        System.out.println("Deleted user with ID: " + userId);
    }
	@Transactional
	public User updateUserAndAddress(Long userId, User user) {

		userRepo.updateUser(userId, user.getUsername(), user.getPassword(), user.getName());
		Address address = user.getAddress();
		if (address != null) {
			userRepo.updateAddress(userId, address.getAddressLine1(), address.getAddressLine2(), address.getCity(),
					address.getRegion(), address.getCountry(), address.getZipCode());
			user = addressService.saveOrUpdateAddress(userId, address.getAddressLine1(), address.getAddressLine2(),
					address.getCity(), address.getRegion(), address.getCountry(), address.getZipCode());
			System.out.println("address=" + address.getCity());
			System.out.println("user address =" + user.getAddress());
			System.out.println("address userId=" + user.getUserId());
			System.out.println("User=" + user);
			return user;

		}

		return user;
	}

	public void saveAccount(Long userId, User user) {

		Account account = new Account();
		account.getUsers().add(user);
		accountRepo.save(account);

		System.out.println("account id=" + account.getAccountId());
		user.getAccounts().add(account);
		userRepo.save(user);

		System.out.println("exit from saveaccount");

	}

	@Transactional
	public Account saveUserAccountById(Long userId, String string) {
		User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		System.out.println("reached saveuseraccountbyid");
		int numberOfAccounts = user.getAccounts().size();
		System.out.println("number of accounts= " + numberOfAccounts);
		Account account = new Account();
		account.setAccountName("Account#" + (numberOfAccounts + 1));
		account.getUsers().add(user);
		user.getAccounts().add(account);
		Account savedAccount = accountRepo.save(account);
		System.out.println("exit from saveuseraccountbyid");
		return savedAccount;
		// System.out.println("accountId="+savedAccount.getAccountId());
		// System.out.println("accountName="+account.getAccountName());

	}

	public User findUserWithAddress(Long userId) {
		return userRepo.findUserwithAddress(userId);
	}

	public User findUserWithAccountAndAddress(Long userId) {

		return userRepo.findUserAccountAndAddress(userId);
	}

	public Account saveUserAccount(Long userId, Long accountId, String accountName) {
		User user = userRepo.findById(userId).orElse(null);
		;
		Account account = accountRepo.findById(accountId).orElse(null);
		;
		if (user != null && account != null) {
			account.setAccountName(accountName);
			account.getUsers().add(user);
			user.getAccounts().add(account);
			accountRepo.save(account);
			userRepo.save(user);
		}
		return account;

	}
}
