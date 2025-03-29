package com.bmdb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Actor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
private String firstName;
private String lastName;
private String gender;
private String birthDate;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
public String getGender() {
	return gender;
}
public void setGender(String gender) {
	this.gender = gender;
}
public String getBirthDate() {
	return birthDate;
}
public void setBirthDate(String birthDate) {
	this.birthDate = birthDate;
}
@Override
public String toString() {
	return "Actor [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
			+ ", birthDate=" + birthDate + "]";
}

}
