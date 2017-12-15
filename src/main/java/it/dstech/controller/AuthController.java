package it.dstech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.dstech.models.User;
import it.dstech.models.UserProfileType;
import it.dstech.services.UserServices;
import it.dstech.services.auth.AuthServices;

@RestController
public class AuthController {

	@Autowired
	private AuthServices authService;
	
	@Autowired
	private UserServices userService;

	@Autowired
	private PasswordEncoder encoder;

	@PostMapping("/login")
	public UserDetails authenticate(@RequestBody User principal) throws Exception {
		return authService.authenticate(principal);
	}

	@PostMapping("/register")
	public User addUser(@RequestBody User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setProfileType(UserProfileType.ROLE_USER);
		return userService.saveUser(user);
	}

	@GetMapping("/getusermodel")
	public User getModel() {
		return new User();
	}
	
	@RequestMapping("/delete")
	public void deleteUser(int id) {
		userService.deleteUser(id);
	}

}
