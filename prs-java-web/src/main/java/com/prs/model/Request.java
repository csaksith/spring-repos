package com.prs.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Request {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "UserId")
	private User user;
	private String requestNumber;
	private String description;
	private String justification;
	private LocalDate dateNeeded;
	private String deliveryMode;
	private String status = "NEW";
	private double total = 0.0;
	private LocalDate submittedDate;
	private String reasonForRejection;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public LocalDate getDateNeeded() {
		return dateNeeded;
	}

	public void setDateNeeded(LocalDate localDate) {
		this.dateNeeded = localDate;
	}

	public String getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public LocalDate getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(LocalDate localDate) {                                                                                                                                                                                                                                                                                                                                                     
		this.submittedDate = localDate;
	}

	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}

	@Override
	public String toString() {
		return "Request [id=" + id + ", user=" + user + ", requestNumber=" + requestNumber + ", description="
				+ description + ", justification=" + justification + ", dateNeeded=" + dateNeeded + ", deliveryMode="
				+ deliveryMode + ", status=" + status + ", total=" + total + ", submittedDate=" + submittedDate
				+ ", reasonForRejection=" + reasonForRejection + "]";
	}

}
