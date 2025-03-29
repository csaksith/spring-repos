package com.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class DemoController {

	@GetMapping("/name")
	public String getFullName(@RequestParam String firstName, @RequestParam String lastName) {
		return "Full Name: "+firstName+" "+lastName;
	}
	
	@GetMapping("/price/{price}/{quantity}")
	public String getTotalPrice(@PathVariable double price, @PathVariable int quantity) {
		double total = price*quantity;
		return "Total: $"+total;
	}
	
	@PostMapping("/add")
	public String addMovie(@RequestBody Movie movie) {
		return "Movie added: "+movie.getTitle()+" directed by: "+movie.getDirector();				
	}
}
