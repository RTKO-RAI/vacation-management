package com.example.vacation_management.controller;

import com.example.vacation_management.dto.VacationResponse;
import com.example.vacation_management.model.Vacation;
import com.example.vacation_management.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vacations")
public class VacationController {

    private final VacationService vacationService;

    @PostMapping("/create")
    public ResponseEntity<Vacation> createVacation(@RequestParam Long userId,
                                   @RequestParam int vacationYear,
                                   @RequestParam int usedDays,
                                   @RequestParam String startDate) {
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate vacationStartDate = LocalDate.parse(startDate, formatter);

            return ResponseEntity.ok(vacationService.createVacation(userId, vacationYear, usedDays, vacationStartDate));
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Please use dd-MM-yyyy");
        }
    }

    @GetMapping("/{userId}/{year}")
    public ResponseEntity<VacationResponse> getVacation(@PathVariable Long userId, @PathVariable int year) {
        return ResponseEntity.ok(vacationService.getRemainingVacationDays(userId, year));
    }

    @PutMapping("/{vacationId}")
    public ResponseEntity<Vacation> updateVacation(@PathVariable Long vacationId, @RequestBody Vacation vacationDetails) {
        return ResponseEntity.ok(vacationService.updateVacation(vacationId, vacationDetails));
    }

    @DeleteMapping("/{vacationId}")
    public void deleteVacation(@PathVariable Long vacationId) {
        vacationService.deleteVacation(vacationId);
    }
}
