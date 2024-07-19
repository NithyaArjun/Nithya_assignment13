package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.UserRepository;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.AddressService;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private AccountService accountService;
	@Autowired
	AccountRepository accountRepo;
	@Autowired
	UserRepository userRepo;

	@GetMapping("/register")
	public String getCreateUser(ModelMap model) {
		model.put("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String postCreateUser(User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}

	@GetMapping("/users")
	public String getAllUsers(ModelMap model) {
		Set<User> users = userService.findAll();
		model.put("users", users);

		if (users.size() == 1) {
			 User user = users.iterator().next();
		        model.put("user", user);
		        model.put("address", user.getAddress() != null ? user.getAddress() : new Address());
		    }

		return "users";
	}

	@GetMapping("/users/{userId}")
	public String getOneUser(ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		model.put("users", Arrays.asList(user));
		model.put("user", user);
		com.coderscampus.assignment13.domain.Address address = addressService.findByUserId(userId);
		if (address == null) {
			address = new com.coderscampus.assignment13.domain.Address(); // Provide a default address object
		}
		model.put("address", address);
		return "users";
	}

	@PostMapping("/users/{userId}")
	public String postOneUser(User user, com.coderscampus.assignment13.domain.Address address) {
		userService.saveUser(user);
		if (address != null) {
			addressService.saveAddress(address);
		}
		return "redirect:/users/" + user.getUserId();
	}

	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser(@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

	@PostMapping("/users/{userId}/update")
	public String updateUser(@PathVariable Long userId, @ModelAttribute User user) {
		User updatedUser = userService.updateUserAndAddress(userId, user);
		return "redirect:/users/" + user.getUserId();
	}

	@GetMapping("/users/{userId}/update")
	public String viewUpdateUser(@PathVariable Long userId, ModelMap model, User user) {
		User updatedUser = userService.findUserWithAddress(userId); // Fetch the updated user
		model.put("user", updatedUser);
		return "/users/" + user.getUserId();
	}

	@PostMapping("/users/{userId}/accounts")
	public String createAccounts(@ModelAttribute User user, @PathVariable Long userId, Account account) {

		Account savedAccount = userService.saveUserAccountById(userId, account.getAccountName());
		return "redirect:/users/" + user.getUserId() + "/account/" + savedAccount.getAccountId();
	}

	@GetMapping("/users/{userId}/account/{accountId}")
	public String getCreateUserAccount(@PathVariable Long userId, ModelMap model, @PathVariable Long accountId) {
		
		User user = userService.findById(userId);
		Account newAccount = accountRepo.findById(accountId).orElse(null);
		model.addAttribute("user", user);
		model.addAttribute("account", newAccount);
		return "/accounts";
	}

	@PostMapping("/users/{userId}/account/{accountId}")
	public String postCreateUserAccount(@PathVariable Long userId, @PathVariable Long accountId,
			@ModelAttribute Account account) {
		User user = userRepo.findById(userId).orElse(null);
		Account existingAccount = accountRepo.findById(accountId).orElse(null);
		if (existingAccount == null) {
			// handle the case where the account doesn't exist
			throw new RuntimeException("Account not found");
		}
		existingAccount.setAccountName(account.getAccountName());
		accountRepo.save(existingAccount);
		return "redirect:/users/" + user.getUserId() + "/account/" + existingAccount.getAccountId();
	}

}