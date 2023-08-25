package com.example.RH.repository;

import com.example.RH.model.Attendance;
import com.example.RH.model.User;
import com.example.RH.wrapper.AttendanceWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Integer> {
    Optional<Attendance> findByDatePointageAndCollaborateur(LocalDate today, User collaborateur);

    List<AttendanceWrapper> getAllAttendance();


    List<AttendanceWrapper> getAllAttendancePerDate(@Param("date") LocalDate Date);

    List<AttendanceWrapper> getAllAttendanceUser(User collaborateur);

    List<AttendanceWrapper> getAllAttendanceUserPerDate(@Param("date") LocalDate Date, User collaborateur);
}
