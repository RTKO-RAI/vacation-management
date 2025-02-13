package com.example.vacation_management.controller;
import com.example.vacation_management.service.WorkHoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workhours")
@RequiredArgsConstructor
public class WorkHoursController {

    private final WorkHoursService workHoursService;

    @PostMapping("/register/{userId}")
    public ResponseEntity<?> registerWorkHours(@PathVariable Long userId, @RequestParam int workedHoursPerDay) {
        return ResponseEntity.ok(workHoursService.registerWorkHours(userId, workedHoursPerDay));
    }

    @GetMapping("/total/{userId}")
    public ResponseEntity<?> getWorkingHours(@PathVariable Long userId) {
        return ResponseEntity.ok(workHoursService.getWorkingHours(userId));

    }
}
