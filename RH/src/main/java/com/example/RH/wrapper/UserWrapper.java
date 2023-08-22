package com.example.RH.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserWrapper {
    private Integer id;
    private String firstName ;
    private String lastName;
    private String email;
    private String status;

    public UserWrapper(Integer id, String firstName, String lastName, String email, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
    }
}
