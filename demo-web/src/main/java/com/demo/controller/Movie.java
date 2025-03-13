package com.demo.controller;

public class Movie {
private String title;
private String director;
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getDirector() {
	return director;
}
public void setDirector(String director) {
	this.director = director;
}
@Override
public String toString() {
	return "Movie [title=" + title + ", director=" + director + "]";
}

}
