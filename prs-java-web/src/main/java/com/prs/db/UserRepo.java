package com.prs.db;
import com.prs.model.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.User;
import org.springframework.stereotype.Repository;
public interface UserRepo extends JpaRepository<User, Integer>{

	Optional<User> findByUsernameAndPassword(String username, String password);

}
