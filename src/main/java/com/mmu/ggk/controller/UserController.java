package com.mmu.ggk.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.ggk.model.Role;
import com.mmu.ggk.model.User;
import com.mmu.ggk.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	// private static final Logger private static final Logger logger =
	// LoggerFactory.getLogger(UserController.class); = (Logger)
	// LoggerFactory.getLogger("UserController.class");
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.findAll();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		if (null != user.getRoles()) {
			newUser.setRoles(user.getRoles());
			for (Role userRoles : user.getRoles()) {

				Long role = userRoles.getId();

				System.out.println("Here is the users Roles>>>>>>>>>>>>>>>>>>>>" + role);

			}
		}

		userService.save(newUser);
		logAudit(newUser.getId(), "CREATE");
		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		User user = userService.findUserById(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
		User user = userService.findUserById(id);
		user.setUsername(userDetails.getUsername());
		user.setEmail(userDetails.getEmail());
		user.setPassword(userDetails.getPassword());
		user.setRoles(userDetails.getRoles());
		User updatedUser = userService.save(user);
		logAudit(updatedUser.getId(), "UPDATE");
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		logAudit(id, "DELETE");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private void logAudit(Long userId, String action) {
		logger.info("User  ID: {}, Action: {}, Performed By: {}, Performed Date: {}", userId, action, "admin",
				LocalDateTime.now());
	}
}
