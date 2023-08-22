package com.example.RH.service;

import com.example.RH.config.JwtAuthenticationFilter;
import com.example.RH.config.JwtService;
import com.example.RH.model.User;
import com.example.RH.repository.UserRepository;
import com.example.RH.utils.EmailUtils;
import com.example.RH.utils.RhUtils;
import com.example.RH.wrapper.UserWrapper;
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
                    return RhUtils.getResponseEntity("user status updated seccussfuly",HttpStatus.OK);
                }else {
                    return RhUtils.getResponseEntity("user does not exist",HttpStatus.OK);
                }
            }else {
                return RhUtils.getResponseEntity("unauthorized",HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
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
                    return RhUtils.getResponseEntity("password updated seccussfuly", HttpStatus.OK);
                }
                return RhUtils.getResponseEntity("old password incorrect", HttpStatus.BAD_REQUEST);
            }
            return RhUtils.getResponseEntity("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepository.findByUserName(requestMap.get("email"));
            if(!Objects.isNull(user) && !user.getEmail().isEmpty()){
                emailUtils.forgotMail(user.getEmail(), "Credentials",user.getPassword());
            }
            return RhUtils.getResponseEntity("check your Email for credentials",HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
