package com.example.RH.repository;

import com.example.RH.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional <User> findByEmail(String email);
}
