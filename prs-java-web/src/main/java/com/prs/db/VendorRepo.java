package com.prs.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.Vendor;

public interface VendorRepo extends JpaRepository<Vendor, Integer>{
}
