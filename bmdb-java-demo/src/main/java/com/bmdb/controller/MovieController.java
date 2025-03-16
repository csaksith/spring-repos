package com.bmdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.bmdb.db.MovieRepo;
import com.bmdb.model.Movie;

@CrossOrigin
@RestController
@RequestMapping("/api/movies")
public class MovieController {

	@Autowired
	private MovieRepo movieRepo;

	@GetMapping("/")
	public List<Movie> getAll() {
		return movieRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Movie> getById(@PathVariable int id) {
		Optional<Movie> m = movieRepo.findById(id);
		if (m.isPresent()) {
			return m;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found for id " + id);
		}
	}

	@PostMapping("")
	public Movie add(@RequestBody Movie movie) {
		return movieRepo.save(movie);
	}

	@PutMapping("/{id}")
	public void putMovie(@PathVariable int id, @RequestBody Movie movie) {
		if (id != movie.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie id mismatch vs URL.");
		} else if (movieRepo.existsById(movie.getId())) {
			movieRepo.save(movie);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found for id " + id);
		}
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		if (movieRepo.existsById(id)) {
			movieRepo.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found for id " + id);
		}
	}

	@GetMapping("/rating/{rating}")
	public List<Movie> getMoviesForRating(@PathVariable String rating) {
		return movieRepo.findByRating(rating);
	}

	@PutMapping("/upd-rating/{id}")
	public void updateRatingForMovie(@PathVariable int id) {
		// update movie rating
		// get movie for id
		if (movieRepo.existsById(id)) {
			Movie m = movieRepo.findById(id).get();
			m.setRating("R");
			movieRepo.save(m);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found for id " + id);
		}
	}
}