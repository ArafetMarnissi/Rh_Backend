package com.example.RH.Auth;

import com.example.RH.config.JwtService;
import com.example.RH.model.Role;
import com.example.RH.model.User;
import com.example.RH.repository.UserRepository;
import com.example.RH.utils.RhUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



  /*  public AuthenticationResponse  authenticate(AuthenticationRequest request) {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user,user.getRole().toString());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();

    }*/
    //////
    public ResponseEntity<String>  authenticate(AuthenticationRequest request) {

        try {
            if (validateAuthRequest(request)) {
                Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
                var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
                var jwtToken = jwtService.generateToken(user, user.getRole().toString());
            /* AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();*/
                if (auth.isAuthenticated()) {
                    if (user.getStatus().equalsIgnoreCase("true")) {
                        return new ResponseEntity<>("{\"token\":\"" + jwtToken + "\"}", HttpStatus.OK);
                    } else {
                        return RhUtils.getResponseEntity("wait for admin approval", HttpStatus.BAD_REQUEST);
                    }
                }
            }else {
                return RhUtils.getResponseEntity("invalid data", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("Somthing Went Wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    //////
public ResponseEntity<String> register(RegisterRequest request) {

   try {
       if (validateRegisterRequest(request)) {
           if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
               var user = User
                       .builder()
                       .firstName(request.getFirstName())
                       .lastName(request.getLastName())
                       .email(request.getEmail())
                       .password(passwordEncoder.encode(request.getPassword()))
                       .status("false")
                       .role(Role.COLLABORATEUR)
                       .build();
               userRepository.save(user);
               var jwtToken = jwtService.generateToken(user,user.getRole().toString());
               return RhUtils.getResponseEntity("seccussfuly registred", HttpStatus.OK);
           } else {
               return RhUtils.getResponseEntity("email already used", HttpStatus.BAD_REQUEST);
           }
       } else {
           return RhUtils.getResponseEntity("invalid data", HttpStatus.BAD_REQUEST);

       }
   }catch (Exception ex){
       ex.printStackTrace();
   }
   return RhUtils.getResponseEntity("something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
}
    //validate that the  request of authentification contains the login and the password
    private boolean validateAuthRequest(AuthenticationRequest request){
        if(request.getEmail().isEmpty() || request.getPassword().isEmpty()){
            return false;
        }
        return true;
    }
    //validate that the  request of Register contains the login and the password
    private boolean validateRegisterRequest(RegisterRequest request){
        if(request.getEmail().isEmpty() ||
                request.getPassword().isEmpty() ||
                request.getLastName().isEmpty() ||
                request.getFirstName().isEmpty()){
            return false;
        }
        return true;
    }
}
