package com.bmdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.bmdb.db.ActorRepo;
import com.bmdb.model.Actor;

@CrossOrigin
@RestController
@RequestMapping("/api/actors")
public class ActorController {

	@Autowired
	private ActorRepo actorRepo;
	
	@GetMapping("/")
	public List<Actor> getAll() {
		return actorRepo.findAll();
	}
	
	@GetMapping("/{id}")
	public Optional<Actor> getById(@PathVariable int id) {
	Optional<Actor> a = actorRepo.findById(id);
		if (a.isPresent()) {
			return a;
		}
		else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Actor not found for id "+id);
		}
	}
	
	@PostMapping("")
	public Actor add(@RequestBody Actor actor) {
		return actorRepo.save(actor);
	}
	
	@PutMapping("/{id}")
	public void putActor(@PathVariable int id, @RequestBody Actor actor) {
        if (id != actor.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Actor id mismatch vs URL.");
        }
        else if (actorRepo.existsById(actor.getId())) {
            actorRepo.save(actor);
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Actor not found for id "+id);
        }
	}
	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		if (actorRepo.existsById(id)) {
			actorRepo.deleteById(id);
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Actor not found for id " + id);
		}
	}
	
}