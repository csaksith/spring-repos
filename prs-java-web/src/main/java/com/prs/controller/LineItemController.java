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
import com.prs.db.ProductRepo;
import com.prs.db.RequestRepo;
import com.prs.model.LineItem;
import com.prs.model.Product;
import com.prs.model.Request;

import jakarta.transaction.Transactional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/line-items")
public class LineItemController {
	@Autowired
	private LineItemRepo lineItemRepo;
	@Autowired
	private RequestRepo requestRepo;
	@Autowired
	private ProductRepo productRepo;

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

		return lineItemRepo.findByRequestId(requestId);
	}
	@CrossOrigin(origins = "http://localhost:4200")

	@PostMapping("/")
	public LineItem add(@RequestBody LineItem lineItem) {
	    System.out.println("Received LineItem: " + lineItem);

		Optional<Request> requestOpt = requestRepo.findById(lineItem.getRequest().getId());
		Optional<Product> productOpt = productRepo.findById(lineItem.getProduct().getId());
		
		if (requestOpt.isPresent() && productOpt.isPresent()) {
			lineItem.setRequest(requestOpt.get());
			lineItem.setProduct(productOpt.get());
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request or Product not found");
		}
		
		
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
	    Optional<LineItem> liOpt = lineItemRepo.findById(id);
	    if (liOpt.isPresent()) {
	        LineItem li = liOpt.get(); // This gets the line item before deleting
	        int requestId=li.getRequest().getId();
	        lineItemRepo.deleteById(id); // Now delete it
	        recalcTotal(li.getRequest().getId()); // Now recalc total using request id
	    } else {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "line item not found");
	    }
	}
}
