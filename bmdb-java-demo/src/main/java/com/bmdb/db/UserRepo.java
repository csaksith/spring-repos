package com.bmdb.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmdb.model.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	Optional<User> findByUsernameAndPassword(String username, String password);
	
}
