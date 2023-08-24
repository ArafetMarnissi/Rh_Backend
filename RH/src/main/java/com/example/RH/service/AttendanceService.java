package com.example.RH.service;

import com.example.RH.config.JwtAuthenticationFilter;
import com.example.RH.model.Attendance;
import com.example.RH.model.User;
import com.example.RH.repository.AttendanceRepository;
import com.example.RH.repository.UserRepository;
import com.example.RH.wrapper.AttendanceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private final AttendanceRepository attendanceRepository;


    public String determinerTypePointage(LocalTime heure) {
        if (heure.isAfter(LocalTime.of(8, 0)) && heure.isBefore(LocalTime.of(9, 0))) {
            return "matin";
        } else if (heure.isAfter(LocalTime.of(11, 0)) && heure.isBefore(LocalTime.of(12, 0))) {
            return "après-midi";
        } else if (heure.isAfter(LocalTime.of(12, 0)) && heure.isBefore(LocalTime.of(13, 0))) {
            return "retour";
        } else if (heure.isAfter(LocalTime.of(16, 0)) && heure.isBefore(LocalTime.of(17, 0))) {
            return "départ";
        } else {
            return "inconnu"; // Gérer les autres cas
        }
    }


    public Attendance updateOrCreateAttendanceForToday(String typePointage,LocalTime heure) {
       try {
           LocalDate today = LocalDate.now();
           User Collaborateur = userRepository.findByUserName(jwtAuthenticationFilter.getCurrentUser());
           Optional<Attendance> attendance = attendanceRepository.findByDatePointageAndCollaborateur(today, Collaborateur);

           if (attendance.isPresent()) {
               Attendance attendanceToUpdate = attendance.get();

               switch (typePointage) {
                   case "matin":
                       if (attendanceToUpdate.getHeureMatin()== null)
                       attendanceToUpdate.setHeureMatin(heure);
                       else {
                           throw new RuntimeException("You have already checked-in");
                       }
                       break;
                   case "après-midi":
                       if (attendanceToUpdate.getHeureApresMidi()== null)
                       attendanceToUpdate.setHeureApresMidi(heure);
                       else {
                           throw new RuntimeException("You have already checked-in");
                       }
                       break;
                   case "retour":
                       if (attendanceToUpdate.getHeureRetour()== null)
                       attendanceToUpdate.setHeureRetour(heure);
                       else {
                           throw new RuntimeException("You have already checked-in");
                       }
                       break;
                   case "départ":
                       if (attendanceToUpdate.getHeureDepart()== null)
                       attendanceToUpdate.setHeureDepart(heure);
                       else {
                           throw new RuntimeException("You have already checked-in");
                       }
                       break;
                   case "inconnu":
                       break;
                   default:
                       // Gestion des erreurs
               }
               // Mettez à jour les champs appropriés de l'attendance existante
               return attendanceToUpdate;
           } else {

               // Créer une nouvelle attendance pour la date d'aujourd'hui
               Attendance createdAttendance = new Attendance();

               createdAttendance.setCollaborateur(Collaborateur);
               createdAttendance.setDatePointage(LocalDate.now());
               switch (typePointage) {
                   case "matin":
                       createdAttendance.setHeureMatin(heure);
                       break;
                   case "après-midi":
                       createdAttendance.setHeureApresMidi(heure);
                       break;
                   case "retour":
                       createdAttendance.setHeureRetour(heure);
                       break;
                   case "départ":
                       createdAttendance.setHeureDepart(heure);
                       break;
                   default:
                       // Gestion des erreurs
               }
               return createdAttendance;
           }


       }catch (Exception ex){
        ex.printStackTrace();
       }
       return null;
    }

    public ResponseEntity<List<AttendanceWrapper>> getAllAttendence() {
        try {
            if(jwtAuthenticationFilter.isAdmin()) {
                return new ResponseEntity<>(attendanceRepository.getAllAttendance(),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<AttendanceWrapper>> getAllAttendencePerDate(Map<String, String> requestMap) {
        try {
            if(jwtAuthenticationFilter.isAdmin()) {
                return new ResponseEntity<>(attendanceRepository.getAllAttendancePerDate(LocalDate.parse(requestMap.get("date"))),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<AttendanceWrapper>> getAllAttendenceUser() {
        try {
            if(jwtAuthenticationFilter.isCollaborateur()) {
                User collaborateur = userRepository.findByUserName(jwtAuthenticationFilter.getCurrentUser());
                return new ResponseEntity<>(attendanceRepository.getAllAttendanceUser(collaborateur),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<AttendanceWrapper>> getAllAttendenceUserPerDate(Map<String, String> requestMap) {
        try {
            if(jwtAuthenticationFilter.isCollaborateur()) {
                User collaborateur = userRepository.findByUserName(jwtAuthenticationFilter.getCurrentUser());
                return new ResponseEntity<>(attendanceRepository.getAllAttendanceUserPerDate(LocalDate.parse(requestMap.get("date")),collaborateur),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
