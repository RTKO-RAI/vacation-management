package com.example.vacation_management.repository;

import com.example.vacation_management.model.User;
import com.example.vacation_management.model.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkHoursRepository extends JpaRepository<WorkHours, Long> {
    List<WorkHours> findByUserIdAndWeekStartDate(Long userId, LocalDate weekStartDate);
    Optional<WorkHours> findByUserAndWeekStartDate(User user, LocalDate weekStartDate);
    @Query("SELECT COALESCE(SUM(w.totalWorkedHours), 0) FROM WorkHours w WHERE w.user = :user AND MONTH(w.weekStartDate) = :month AND YEAR(w.weekStartDate) = :year")
    int findTotalWorkedHoursByUserAndMonth(@Param("user") User user, @Param("month") int month, @Param("year") int year);


}