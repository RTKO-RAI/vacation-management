package com.example.vacation_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VacationResponse {
    private String firstName;
    private String lastName;
    private int remainingVacationDays;
}
