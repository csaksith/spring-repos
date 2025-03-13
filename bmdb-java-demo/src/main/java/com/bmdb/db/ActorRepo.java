package com.bmdb.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmdb.model.Actor;

public interface ActorRepo extends JpaRepository<Actor, Integer> {

}
