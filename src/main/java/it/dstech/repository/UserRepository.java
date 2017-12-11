package it.dstech.repository;

import org.springframework.data.repository.CrudRepository;

import it.dstech.models.User;

public interface UserRepository extends CrudRepository<User, Integer>  {
	User findByUsername(String username);
	
	User findById(int id);
}
