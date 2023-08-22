package com.example.RH.controller;

import com.example.RH.model.Attendance;
import com.example.RH.repository.AttendanceRepository;
import com.example.RH.service.AttendanceService;
import com.example.RH.utils.RhUtils;
import com.example.RH.wrapper.AttendanceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class AttendanceController implements AttendanceControllerInt{
    @Autowired
    private final AttendanceService attendanceService;
    @Autowired
    private  final AttendanceRepository attendanceRepository;
    @Override
    public ResponseEntity<String> pointage() {
        try {
            //determiner l heure de maintenant
            var now = new Date();
            String heureActuelle = String.format("%02d:%02d", now.getHours(), now.getMinutes());
            LocalTime heure = LocalTime.parse(heureActuelle, DateTimeFormatter.ofPattern("HH:mm"));

//
            String typePointage = attendanceService.determinerTypePointage(heure);
            if (Objects.equals(typePointage, "inconnu")){
                return RhUtils.getResponseEntity("À ce moment, il n'est pas autorisé de faire une pointe.", HttpStatus.OK);
            }else {
            Attendance attendance = attendanceService.updateOrCreateAttendanceForToday(typePointage,heure);
            attendanceRepository.save(attendance);
            return RhUtils.getResponseEntity("pointage ajouté avec succés", HttpStatus.OK);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RhUtils.getResponseEntity("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<AttendanceWrapper>> GetAllPointageAdmin() {
        try {
            return attendanceService.getAllAttendence();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<AttendanceWrapper>> GetAllPointageUser() {
        try {
            return attendanceService.getAllAttendenceUser();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<AttendanceWrapper>> GetAllPointageAdminPerDate(@RequestBody(required = true) Map<String,String> requestMap) {
        try {
            return attendanceService.getAllAttendencePerDate(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<AttendanceWrapper>> GetAllPointageUserPerDate(@RequestBody(required = true) Map<String,String> requestMap) {
        try {
            return attendanceService.getAllAttendenceUserPerDate(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
