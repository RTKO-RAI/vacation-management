package com.example.vacation_management.controller;

import com.example.vacation_management.dto.VacationResponse;
import com.example.vacation_management.model.Vacation;
import com.example.vacation_management.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vacations")
public class VacationController {

    private final VacationService vacationService;

    @PostMapping("/create")
    public Vacation createVacation(@RequestParam Long userId,
                                   @RequestParam int vacationYear,
                                   @RequestParam int usedDays) {
        return vacationService.createVacation(userId, vacationYear, usedDays);
    }

    @GetMapping("/{userId}/{year}")
    public VacationResponse getVacation(@PathVariable Long userId, @PathVariable int year) {
        return vacationService.getRemainingVacationDays(userId, year);
    }

    @PutMapping("/{vacationId}")
    public Vacation updateVacation(@PathVariable Long vacationId, @RequestBody Vacation vacationDetails) {
        return vacationService.updateVacation(vacationId, vacationDetails);
    }

    @DeleteMapping("/{vacationId}")
    public void deleteVacation(@PathVariable Long vacationId) {
        vacationService.deleteVacation(vacationId);
    }
}
