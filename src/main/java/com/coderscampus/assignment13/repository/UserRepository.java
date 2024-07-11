package com.coderscampus.assignment13.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// select * from users where username = :username
	List<User> findByUsername(String username);

	// select * from users where name = :name
	List<User> findByName(String name);

	// select * from users where name = :name and username = :username
	List<User> findByNameAndUsername(String name, String username);

	List<User> findByCreatedDateBetween(LocalDate date1, LocalDate date2);

	@Query("select u from User u where username = :username")
	List<User> findExactlyOneUserByUsername(String username);

	@Query("select u from User u" + " left join fetch u.accounts" + " left join fetch u.address")
	Set<User> findAllUsersWithAccountsAndAddresses();

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.username = :username, " + "u.password = :password, " + "u.name = :name "
			+ "WHERE u.userId = :userId")
	void updateUser(@Param("userId") Long userId, @Param("username") String username,
			@Param("password") String password, @Param("name") String name);

	@Modifying
	@Transactional
	@Query("UPDATE Address a SET a.addressLine1 = :addressLine1, " + "a.addressLine2 = :addressLine2, "
			+ "a.city = :city, " + "a.region = :region, " + "a.country = :country, " + "a.zipCode = :zipCode "
			+ "WHERE a.userId = :userId")
	void updateAddress(@Param("userId") Long userId, @Param("addressLine1") String addressLine1,
			@Param("addressLine2") String addressLine2, @Param("city") String city, @Param("region") String region,
			@Param("country") String country, @Param("zipCode") String zipCode);

	
	@Query("select u from User u" + " left join fetch u.accounts" + " left join fetch u.address"
			+ " WHERE u.userId=:userId ")
	User findUserAccountAndAddress(@Param("userId") Long userId);

	@Query("select u from User u" + " left join fetch u.address" + " WHERE u.userId=:userId ")
	User findUserwithAddress(@Param("userId") Long userId);

	void save(Account account);

}