package com.hello.controller;

import org.springframework.web.bind.annotation.*;

import com.hello.model.Movie;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

	@GetMapping("/")
	public String sayHello() {
		return "Hello World!";
	}

	@GetMapping("/{nbr}")
	public String sayHello(@PathVariable int nbr) {
		return "Hello number " + nbr;
	}

	@PostMapping("/")
	public String addMovie(@RequestBody Movie movie) {
	return movie.toString();
} 
	@GetMapping("/get-name-age/{name}/{age}")
	public String getNameAndAge(@PathVariable String name, @PathVariable int age){
		return "Hello, "+name+". I am "+age+" years old.";
	}
	@GetMapping("/stuff")
		public String getStuff(@RequestParam String var1, String var2, String var3) {
			return "Stuff: " +var1+", "+var2+", "+var3;
		}
	}

