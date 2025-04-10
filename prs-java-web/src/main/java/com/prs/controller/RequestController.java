package com.prs.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.prs.db.RequestRepo;
import com.prs.db.UserRepo;
import com.prs.model.Request;
import com.prs.model.RequestDTO;
import com.prs.model.User;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/requests")
public class RequestController {
	@Autowired
	private RequestRepo requestRepo;
@Autowired
private UserRepo userRepo;
	@GetMapping("/")
	public List<Request> getAllRequests() {
		return requestRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Request> getById(@PathVariable int id) {
		Optional<Request> r = requestRepo.findById(id);
		if (r.isPresent()) {
			return r;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found for id: " + id);
		}
	}

	@GetMapping("/list-review/{userId}")
	public List<Request> listReview(@PathVariable int userId) {
		List<Request> r = requestRepo.findByUserIdAndStatus(userId, "REVIEW");
		if (r.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found for UserId: " + userId);
		} else {
			return r;
		}
	}

	public String getRequestNumber() {
		String R = "R";
		String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		// get latest reqnbr for today
		Optional<String> latestReqNbr = requestRepo.findLatestRequestNumber(datePrefix);
		String newReqNbr;
		if (latestReqNbr.isPresent()) {
			int last4Nbrs = Integer.parseInt(latestReqNbr.get().substring(9)) + 1;
			newReqNbr = String.format("%04d", last4Nbrs);
		} else {
			newReqNbr = "0001";
		}
		return R + datePrefix + newReqNbr;
	}

	private boolean nextNbr(String string) {
		// TODO Auto-generated method stub
		return false;
	}

	@PutMapping("/submit-review/{id}")
	public Request submitReview(@PathVariable int id) {
		Optional<Request> optRequest = requestRepo.findById(id);
		if (!optRequest.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found.");
		}

		Request request = optRequest.get();

		if (!"NEW".equals(request.getStatus())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request already submitted for review.");
		}

		// request status automatically approved if total is less than $50
		request.setStatus(request.getTotal() <= 50.0 ? "APPROVED" : "REVIEW");
		request.setSubmittedDate(LocalDate.now());
		return requestRepo.save(request);
	}

	@PutMapping("/approve/{id}")
	public Request approve(@PathVariable int id) {
		Optional<Request> optRequest = requestRepo.findById(id);
		if (!optRequest.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found.");
		} else {
			Request request = optRequest.get();
			request.setStatus("APPROVED");
			request.setSubmittedDate(LocalDate.now());
			return requestRepo.save(request);
		}
	}

	@PutMapping("/reject/{id}")
	public Request reject(@PathVariable int id, @RequestBody Map<String, String> reason) {
		Optional<Request> optRequest = requestRepo.findById(id);
		if (!optRequest.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found.");
		} else {
			Request request = optRequest.get();
			request.setStatus("REJECTED");
			request.setReasonForRejection(reason.get("reasonForRejection"));
			request.setSubmittedDate(LocalDate.now());
			return requestRepo.save(request);
		}
	}

	@PostMapping("")

	public ResponseEntity<Request> createRequest(@RequestBody RequestDTO requestDTO) {
		// Create a new Request object using DTO data
		Request request = new Request();
		// Fetch the user by ID
		Optional<User> optionalUser = userRepo.findById(requestDTO.getUserId());
		if (optionalUser.isEmpty()) {
			return ResponseEntity.badRequest().body(null); // Return 400 if user not found
		}
		// Set the user to the request
		User user = optionalUser.get();
		request.setUser(user); // Correctly mapping the user
		// Set other fields from DTO
		request.setRequestNumber(getRequestNumber());
		request.setDescription(requestDTO.getDescription());
		request.setJustification(requestDTO.getJustification());
		request.setDateNeeded(requestDTO.getDateNeeded());
		request.setDeliveryMode(requestDTO.getDeliveryMode());
		request.setStatus("NEW"); // Default to "NEW"
		request.setTotal(0.0); // Default to 0.0
		request.setSubmittedDate(LocalDate.now()); // Automatically set today's date
		request.setReasonForRejection(null);
		// Save the request to the database
		requestRepo.save(request);
		// Return success response with the created request
		return ResponseEntity.ok(request);

	}

	@PutMapping("/{id}")
	public void putRequest(@PathVariable int id, @RequestBody Request request) {
		if (id != request.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request id mismatch vs URL.");
		} else if (requestRepo.existsById(request.getId())) {
			request.setSubmittedDate(LocalDate.now());
			requestRepo.save(request);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found for id " + id);
		}
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		if (requestRepo.existsById(id)) {
			requestRepo.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "request not found for id " + id);
		}
	}
}
