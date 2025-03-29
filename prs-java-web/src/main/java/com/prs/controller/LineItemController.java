package com.prs.controller;

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

import com.prs.db.LineItemRepo;
import com.prs.db.RequestRepo;
import com.prs.model.LineItem;
import com.prs.model.Request;

import jakarta.transaction.Transactional;

@CrossOrigin
@RestController
@RequestMapping("/api/line-items")
public class LineItemController {
	@Autowired
	private LineItemRepo lineItemRepo;
	@Autowired
	private RequestRepo requestRepo;

	@Transactional
	public void recalcTotal(int requestId) {
		Request request = requestRepo.findById(requestId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
		List<LineItem> lineItems = lineItemRepo.findByRequestId(requestId);
		double total = lineItems.stream()
				.mapToDouble(li -> li.getProduct() != null ? li.getProduct().getPrice() * li.getQuantity() : 0).sum();
		request.setTotal(total);
		request.setStatus(total <= 50 ? "APPROVED" : "NEW");
		requestRepo.save(request);
	}

	@GetMapping("/")
	public List<LineItem> getAllLineItems() {
		return lineItemRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<LineItem> getById(@PathVariable int id) {
		Optional<LineItem> li = lineItemRepo.findById(id);
		if (li.isPresent()) {
			return li;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Line Item not found for id: " + id);
		}
	}

	@GetMapping("/lines-for-req/{requestId}")
	public List<LineItem> findByRequestId(@PathVariable int requestId) {
		List<LineItem> li = lineItemRepo.findByRequestId(requestId);
		if (li.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Line Item not found for id: " + requestId);
		} else {
			return li;
		}
	}

	@PostMapping("")
	public LineItem add(@RequestBody LineItem lineItem) {
		LineItem savedLineItem = lineItemRepo.save(lineItem);
		recalcTotal(savedLineItem.getRequest().getId());
		return savedLineItem;
	}

	
	
	@PutMapping("/{id}")
	public void putLineItem(@PathVariable int id, @RequestBody LineItem lineItem) {
		if (id != lineItem.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "line item id mismatch vs URL.");
		} else if (lineItemRepo.existsById(lineItem.getId())) {
			lineItemRepo.save(lineItem);
			recalcTotal(lineItem.getRequest().getId());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "item item not found for id " + id);
		}
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		if (lineItemRepo.existsById(id)) {
			lineItemRepo.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "line item not found");
		}
	}
}
