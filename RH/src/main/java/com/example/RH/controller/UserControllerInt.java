package com.example.RH.controller;

import com.example.RH.wrapper.UserWrapper;
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
    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody(required = true)Map<String,String> requestMap);
    @GetMapping("/checkToken")

    public ResponseEntity<String> checkToken();
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String,String> requestMap);
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String,String> requestMap);
}
