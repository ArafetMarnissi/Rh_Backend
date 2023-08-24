package com.example.RH.controller;

import com.example.RH.wrapper.UserWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RequestMapping("/api/user")

public interface UserControllerInt {
    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserWrapper>> getAllUser();
    @GetMapping("/getUserById")
    public ResponseEntity<UserWrapper> getUserById(@RequestParam(name = "userId",required = true) Integer userId);
    @GetMapping("/getCurrentUser")
    public ResponseEntity<UserWrapper> getCurrentUser();
    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody(required = true)Map<String,String> requestMap);
    @PostMapping("/updateProfile")
    public ResponseEntity<String> updateProfile(@RequestBody(required = true)Map<String,String> requestMap);
    @GetMapping("/checkToken")

    public ResponseEntity<String> checkToken();
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String,String> requestMap);
    @PostMapping("/createNewPassword")
    public ResponseEntity<String> CreateNewPassword(@RequestBody Map<String,String> requestMap, HttpServletRequest request);
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String,String> requestMap);
}
