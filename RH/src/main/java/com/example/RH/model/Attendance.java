package com.example.RH.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@NamedQuery(name = "Attendance.findByDatePointageAndCollaborateur",
        query = "SELECT a FROM Attendance a WHERE a.datePointage = :today AND a.collaborateur = :collaborateur")
@NamedQuery(name="Attendance.getAllAttendance",query="select new com.example.RH.wrapper.AttendanceWrapper(a.id,a.collaborateur.email, a.collaborateur.firstName,a.datePointage,a.heureMatin,a.heureApresMidi,a.heureRetour,a.heureDepart) from Attendance a")
@NamedQuery(name="Attendance.getAllAttendancePerDate",query="select new com.example.RH.wrapper.AttendanceWrapper(a.id,a.collaborateur.email, a.collaborateur.firstName,a.datePointage,a.heureMatin,a.heureApresMidi,a.heureRetour,a.heureDepart) from Attendance a WHERE a.datePointage=:date")
@NamedQuery(name="Attendance.getAllAttendanceUser",query="select new com.example.RH.wrapper.AttendanceWrapper(a.id,a.collaborateur.email, a.collaborateur.firstName,a.datePointage,a.heureMatin,a.heureApresMidi,a.heureRetour,a.heureDepart) FROM Attendance a WHERE  a.collaborateur = :collaborateur")
@NamedQuery(name="Attendance.getAllAttendanceUserPerDate",query="select new com.example.RH.wrapper.AttendanceWrapper(a.id,a.collaborateur.email, a.collaborateur.firstName,a.datePointage,a.heureMatin,a.heureApresMidi,a.heureRetour,a.heureDepart) from Attendance a WHERE a.datePointage=:date AND a.collaborateur = :collaborateur")



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User collaborateur;

    private LocalDate datePointage;
    private LocalTime heureMatin;
    private LocalTime heureApresMidi;
    private LocalTime heureRetour;
    private LocalTime heureDepart;
}
