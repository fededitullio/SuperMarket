package it.dstech.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dstech.models.User;
import it.dstech.repository.UserRepository;

@Service
public class UserServicesImpl implements UserServices {

	@Autowired
	private UserRepository userRepo;

	@Override
	public User saveUser(User user) {
		return userRepo.save(user);
	}

	@Override
	public User findByUsername(String username) {
		return userRepo.findByUsername(username);
	}
	
	@Override
	public User findById(int id) {
		return userRepo.findOne(id);
	}
	
	@Override
	public void deleteUser(int id) {
		userRepo.delete(id);
	}

}
