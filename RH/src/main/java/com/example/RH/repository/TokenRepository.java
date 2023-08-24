package com.example.RH.repository;

import com.example.RH.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Integer> {
    public Optional<Token> findByToken(String token);
}
