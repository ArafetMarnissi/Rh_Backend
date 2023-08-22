package com.example.RH.controller;

import com.example.RH.service.UserService;
import com.example.RH.utils.RhUtils;
import com.example.RH.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerInt{
private final UserService userService;


    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser(){
        try{
            return userService.getAllUser();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            return userService.update(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return userService.checkToken();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR );

    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            return userService.chagePassword(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            return userService.forgotPassword(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
