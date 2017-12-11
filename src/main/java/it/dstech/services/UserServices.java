package it.dstech.services;

import java.util.List;


import it.dstech.models.User;

public interface UserServices {
	
	User saveUser(User user);
	
	User getUserById(int id);
	
	List<User> getListUsers();

	
	void deleteUser(int id);
	
	
	User findByUsername(String username);
	

}
