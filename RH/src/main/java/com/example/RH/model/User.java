package com.example.RH.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
@NamedQuery(name="User.getAllUser",query="select new com.example.RH.wrapper.UserWrapper(u.id,u.firstName,u.lastName,u.email,u.status) from User u where u.role ='COLLABORATEUR'")
@NamedQuery(name="User.updateStatus",query="update User u set u.status=:status where u.id=:id")
@NamedQuery(name="User.getAllAdmin",query="select u.email from User u where u.role ='ADMIN'")
@NamedQuery(name="User.findByUserName",query="select u from User u where u.email =:email")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
    private String firstName ;
    private String lastName;
    private String email;
    private String password;
    private String status;
    @Enumerated(EnumType.STRING)
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
