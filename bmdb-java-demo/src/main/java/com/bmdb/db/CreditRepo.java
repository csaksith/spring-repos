package com.bmdb.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmdb.model.Credit;
import java.util.List;
public interface CreditRepo extends JpaRepository<Credit, Integer>{
	List<Credit> findAllByMovieId(int movieId);
	List<Credit> findAllByActorId(int actorId);

}
