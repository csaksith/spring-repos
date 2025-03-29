package com.bmdb.model;

import jakarta.persistence.*;

@Entity
public class Credit {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private int id;
@ManyToOne
@JoinColumn(name="ActorId")
private Actor actor;
@ManyToOne
@JoinColumn(name="MovieId")
private Movie movie;
private String role;

public Credit() {
	super();
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public Actor getActor() {
	return actor;
}

public void setActor(Actor actor) {
	this.actor = actor;
}

public Movie getMovie() {
	return movie;
}

public void setMovie(Movie movie) {
	this.movie = movie;
}

public String getRole() {
	return role;
}

public void setRole(String role) {
	this.role = role;
}

@Override
public String toString() {
	return "Credit [id=" + id + ", actor=" + actor + ", movie=" + movie + ", role=" + role + "]";
}



}
