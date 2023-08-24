package com.example.RH.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Token {
    @Id
    @GeneratedValue
    private Integer id;
    private String token ;
    private Boolean isUsed;

    public Token(String token, Boolean isUsed) {
        this.token = token;
        this.isUsed = isUsed;
    }
}
