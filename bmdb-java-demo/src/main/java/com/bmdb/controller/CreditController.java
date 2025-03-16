package com.bmdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bmdb.db.CreditRepo;
import com.bmdb.model.Credit;
import com.bmdb.model.Movie;
@CrossOrigin
@RestController
@RequestMapping("/api/credits")
public class CreditController {
	@Autowired
	private CreditRepo creditRepo;
	
	@GetMapping("/")
	public List<Credit> getAllCredits() {
		return creditRepo.findAll();
	}
	
	@GetMapping("/{id}")
	public Optional<Credit> getById(@PathVariable int id){
	Optional<Credit> c = creditRepo.findById(id);
	if(c.isPresent()) {
		return c;
	}
	else {
		throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Credit not found for id "+id);
	}
	}
	@PostMapping("")
	public Credit add(@RequestBody Credit credit) {
		return creditRepo.save(credit);
	}
	
	@PutMapping("/{id}")
	public void putCredit(@PathVariable int id, @RequestBody Credit credit) {
		if (id != credit.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit id mismatch vs URL.");
		}
		else if (creditRepo.existsById(credit.getId())) {
			creditRepo.save(credit);
		}
		else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Movie not found for id "+id);
		}
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		if (creditRepo.existsById(id)) {
			creditRepo.deleteById(id);
		}
		else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "credit not found for id "+id);
		}
	}
}


