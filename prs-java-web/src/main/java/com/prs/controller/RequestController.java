package com.prs.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.prs.db.RequestRepo;
import com.prs.model.Request;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestController {
	@Autowired
	private RequestRepo requestRepo;

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

	@GetMapping("/list-review/{UserId}")
	public List<Request> listReview(@PathVariable int UserId) {
		List<Request> r = requestRepo.findByIdAndStatus(UserId, "REVIEW");
		if (r.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found for UserId: " + UserId);
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
			int last4Nbrs = Integer.parseInt(latestReqNbr.get().substring(7)) + 1;
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

	@PostMapping("")
	public Request add(@RequestBody Request request) {
		// generate new request number
		request.setRequestNumber(getRequestNumber());
		request.setSubmittedDate(LocalDate.now());
		return requestRepo.save(request);
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
