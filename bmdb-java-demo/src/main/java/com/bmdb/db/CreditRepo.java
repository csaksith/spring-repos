package com.bmdb.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmdb.model.Credit;

public interface CreditRepo extends JpaRepository<Credit, Integer>{

}
