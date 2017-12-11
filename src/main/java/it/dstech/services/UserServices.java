package it.dstech.services;

import java.util.List;


import it.dstech.models.User;

public interface UserServices {
	

	User saveUser(User user);

	User findByUsername(String username);
	
	User findById(int id);
	
	void deleteUser(int id);
	
	
}
