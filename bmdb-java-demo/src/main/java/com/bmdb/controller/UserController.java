package com.bmdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bmdb.db.UserRepo;
import com.bmdb.model.Movie;
import com.bmdb.model.User;
import com.bmdb.model.UserDTO;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepo userRepo;
	
	@GetMapping("/")
	public List<User> getAll() {
		return userRepo.findAll();
	}
	
	@PostMapping("/login")
	public User login(@RequestBody UserDTO userDto) {
		Optional<User> u = userRepo.findByUsernameAndPassword(userDto.getUsername(), userDto.getPassword());
		if (u.isPresent()) {
			return u.get();
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for ");
		}
	}
}
