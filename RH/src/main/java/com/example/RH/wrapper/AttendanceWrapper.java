package com.example.RH.wrapper;

import com.example.RH.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Data
public class AttendanceWrapper {
    private Long id;
    private String email;
    private String firstName;

    private LocalDate datePointage;
    private LocalTime heureMatin;
    private LocalTime heureApresMidi;
    private LocalTime heureRetour;
    private LocalTime heureDepart;

    public AttendanceWrapper(Long id,
                             String email,
                             String firstName,
                             LocalDate datePointage,
                             LocalTime heureMatin,
                             LocalTime heureApresMidi,
                             LocalTime heureRetour,
                             LocalTime heureDepart) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.datePointage = datePointage;
        this.heureMatin = heureMatin;
        this.heureApresMidi = heureApresMidi;
        this.heureRetour = heureRetour;
        this.heureDepart = heureDepart;
    }
}
