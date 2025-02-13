package com.example.vacation_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkHoursResponse {
    private Long userId;
    private int totalWorkedHours;
    private LocalDate weekStartDate;
}
