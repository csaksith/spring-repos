package com.prs.db;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.Request;

public interface RequestRepo extends JpaRepository<Request, Integer> {
	default Optional<String> findLatestRequestNumber(String datePrefix) {
		return findAll().stream().map(Request::getRequestNumber).filter(reqNbr -> reqNbr.startsWith("R" + datePrefix))
				.max(Comparator.naturalOrder());
	}

	List<Request> findByStatusAndUserIdNot(String status, int userId);
}
