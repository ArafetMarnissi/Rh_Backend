package com.example.RH.service;

import com.example.RH.model.Token;
import com.example.RH.model.User;
import com.example.RH.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    public boolean isTokenUsed(String token) {
        try {
            Optional<Token> optional = tokenRepository.findByToken(token);
            if (optional.isPresent()){
                return optional.get().getIsUsed();
            }
        }catch (RuntimeException ex){
            throw new RuntimeException("token does not exist");
        }
        return true;
    }
    public void markTokenAsUsed(String token) {
        try {
            Optional<Token> optional = tokenRepository.findByToken(token);
            if (optional.isPresent()){
                optional.get().setIsUsed(true);
                tokenRepository.save(optional.get());
            }
        }catch (RuntimeException ex){
            throw new RuntimeException("token does not exist");
        }
    }
}
