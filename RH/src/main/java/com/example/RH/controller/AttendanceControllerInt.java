package com.example.RH.controller;

import com.example.RH.wrapper.AttendanceWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/pointage")
public interface AttendanceControllerInt {
    @GetMapping("/pointer")
    public ResponseEntity<String> pointage();
    @GetMapping("/GetAllPointageAdmin")
    public ResponseEntity<List<AttendanceWrapper>> GetAllPointageAdmin();

    @GetMapping("/GetAllPointageUser")
    public ResponseEntity<List<AttendanceWrapper>> GetAllPointageUser();
    @GetMapping("/GetAllPointageAdminPerDate")
    public ResponseEntity<List<AttendanceWrapper>> GetAllPointageAdminPerDate(@RequestParam(required = true) Map<String,String> requestMap);
    @GetMapping("/GetAllPointageUserPerDate")
    public ResponseEntity<List<AttendanceWrapper>> GetAllPointageUserPerDate(@RequestParam(required = true) Map<String,String> requestMap);

}
