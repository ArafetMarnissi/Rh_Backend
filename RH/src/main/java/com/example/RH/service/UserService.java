package com.example.RH.service;

import com.example.RH.config.JwtAuthenticationFilter;
import com.example.RH.config.JwtService;
import com.example.RH.model.Token;
import com.example.RH.model.User;
import com.example.RH.repository.TokenRepository;
import com.example.RH.repository.UserRepository;
import com.example.RH.utils.EmailUtils;
import com.example.RH.utils.RhUtils;
import com.example.RH.wrapper.UserWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;

@Autowired
    EmailUtils emailUtils;
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if(jwtAuthenticationFilter.isAdmin()) {
                return new ResponseEntity<>(userRepository.getAllUser(),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Map<String, String> requestMap) {
        String status="true";
        try {
            if(jwtAuthenticationFilter.isAdmin()) {
                Optional<User> optional = userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (!optional.isEmpty()){
                    if(Objects.equals(optional.get().getStatus(), "false")){
                        status="true";
                    }else {
                        status="false";
                    }

                    userRepository.updateStatus(status,Integer.parseInt(requestMap.get("id")));
                    //sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userRepository.getAllAdmin());
                    return RhUtils.getResponseEntity(optional.get().getLastName()+" Status updated seccussfuly",HttpStatus.OK);
                }else {
                    return RhUtils.getResponseEntity("User does not exist",HttpStatus.OK);
                }
            }else {
                return RhUtils.getResponseEntity("Unauthorized",HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtAuthenticationFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sentSimpleMessage(jwtAuthenticationFilter.getCurrentUser(),
                    "Account Approved","USER:- "+ user +" \n is approved by \n ADMIN:-"+jwtAuthenticationFilter.getCurrentUser() ,
                    allAdmin);
        }else {
            emailUtils.sentSimpleMessage(jwtAuthenticationFilter.getCurrentUser(),
                    "Account Disabled","USER:- "+ user +" \n is disabled by \n ADMIN:-"+jwtAuthenticationFilter.getCurrentUser() ,
                    allAdmin);
        }
    }

    public ResponseEntity<String> checkToken() {
        return RhUtils.getResponseEntity("true",HttpStatus.OK);
    }

    public ResponseEntity<String> chagePassword(Map<String, String> requestMap) {

        try {
            User userObj = userRepository.findByUserName(jwtAuthenticationFilter.getCurrentUser());
            if (!userObj.equals(null)){
                if (passwordEncoder.matches(requestMap.get("oldPassword"), userObj.getPassword())){
                    userObj.setPassword(passwordEncoder.encode(requestMap.get("newPassword")));
                    userRepository.save(userObj);
                    return RhUtils.getResponseEntity("Password updated seccussfuly", HttpStatus.OK);
                }
                return RhUtils.getResponseEntity("Old password incorrect", HttpStatus.BAD_REQUEST);
            }
            return RhUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepository.findByUserName(requestMap.get("email"));
            if(!Objects.isNull(user) && !user.getEmail().isEmpty()){
                String token =  jwtService.generateTokenWithValidaity(new HashMap<>(),user, 1000*60*5);
                Token tokenInstance = new Token(token,false);
                tokenRepository.save(tokenInstance);
                emailUtils.forgotMail(user.getEmail(), "Credentials",token);
                return RhUtils.getResponseEntity("Check your email for credentials !",HttpStatus.OK);
            }else {
                return RhUtils.getResponseEntity("Email does not exist !",HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR );
    }

    public ResponseEntity<String> updateProfile(Map<String, String> requestMap) {
        try {
            if(jwtAuthenticationFilter.isAdmin()) {
                Optional<User> optional = userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (!optional.isEmpty()){
                    optional.get().setFirstName(requestMap.get("firstName"));
                    optional.get().setLastName(requestMap.get("lastName"));
                    optional.get().setEmail(requestMap.get("email"));
                    userRepository.save(optional.get());
                    return RhUtils.getResponseEntity(optional.get().getLastName()+" profile updated seccussfuly",HttpStatus.OK);
                }else {
                    return RhUtils.getResponseEntity("user does not exist",HttpStatus.OK);
                }
            }else {
                return RhUtils.getResponseEntity("unauthorized",HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<UserWrapper> getUserById(Integer userId) {
        try {
            if(jwtAuthenticationFilter.isAdmin()) {
                Optional<User> optional = userRepository.findById(userId);
                if (!optional.isEmpty()) {
                    return new ResponseEntity<>(userRepository.getUserById(userId), HttpStatus.OK);
                }else {
                    throw new RuntimeException("User Not Found");
                }
            }else {
                return new ResponseEntity<>(new UserWrapper(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new UserWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<UserWrapper> getCurrentUser() {
        try {

                Optional<User> optional = userRepository.findByEmail(jwtAuthenticationFilter.getCurrentUser());
                if (!optional.isEmpty()) {
                    return new ResponseEntity<>(userRepository.getUserById(optional.get().getId()), HttpStatus.OK);
                }else {
                    throw new RuntimeException("User Not Found");
                }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new UserWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

 public ResponseEntity<String> createNewPassword(Map<String, String> requestMap, HttpServletRequest request) {
     try {
         String token = jwtAuthenticationFilter.getToken(request); // Get the token from the request

         if (token != null && !tokenService.isTokenUsed(token) ) {
             User userObj = userRepository.findByUserName(jwtAuthenticationFilter.getCurrentUser());
             if (userObj != null) {
                 userObj.setPassword(passwordEncoder.encode(requestMap.get("newPassword")));
                 userRepository.save(userObj);

                 tokenService.markTokenAsUsed(token); // Mark the token as used

                 return RhUtils.getResponseEntity("Password updated successfully", HttpStatus.OK);
             }
         }

         return RhUtils.getResponseEntity("this link is expired ! try again", HttpStatus.BAD_REQUEST);

     } catch (Exception ex) {
         ex.printStackTrace();
     }
     return RhUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
 }

}
