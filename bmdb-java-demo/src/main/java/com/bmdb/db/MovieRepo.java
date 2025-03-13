package com.bmdb.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmdb.model.Movie;

public interface MovieRepo extends JpaRepository<Movie, Integer>{

}