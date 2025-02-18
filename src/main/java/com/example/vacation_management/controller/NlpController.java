package com.example.vacation_management.controller;

import com.example.vacation_management.dto.UserRequest;
import com.example.vacation_management.dto.VacationResponse;
import com.example.vacation_management.dto.WorkHoursResponse;
import com.example.vacation_management.model.*;
import com.example.vacation_management.nlpService.NlpService;
import com.example.vacation_management.service.VacationService;
import com.example.vacation_management.service.WorkHoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/nlp")
@RequiredArgsConstructor
public class NlpController {

    private final NlpService nlpService;
    private final VacationService vacationService;
    private final WorkHoursService workHoursService;

    @PostMapping("/process")
    public ResponseEntity<GeneralResponse> processRequest(@RequestBody UserRequest userRequest) {
        Long userId = userRequest.getUserId();
        String userInput = userRequest.getUserInput();

        String intent = nlpService.getIntent(userInput);

        switch (intent) {
            case "leave_days_left":
                return handleLeaveDaysLeft(userId);

            case "leave_request":
                return handleLeaveRequest(userId, userInput);

            case "register_hours":
                return handleRegisterHours(userId, userInput);

            case "get_total_hours":
                return handleGetTotalHours(userId);

            default:
                return ResponseEntity.badRequest().body(new GeneralResponse("Sorry, I couldn't understand your request."));
        }
    }

    private ResponseEntity<GeneralResponse> handleLeaveDaysLeft(Long userId) {
        VacationResponse vacationResponse = vacationService.getRemainingVacationDays(userId, LocalDate.now().getYear());
        String message = String.format("%s %s, you have %d leave days left.",
                vacationResponse.getFirstName(),
                vacationResponse.getLastName(),
                vacationResponse.getRemainingVacationDays());

        return ResponseEntity.ok(new GeneralResponse(message));
    }

    private ResponseEntity<GeneralResponse> handleLeaveRequest(Long userId, String userInput) {
        LeaveDate leaveDate = nlpService.getLeaveDates(userInput);
        if (leaveDate.getFromDate() == null || leaveDate.getToDate() == null) {
            return ResponseEntity.badRequest().body(new GeneralResponse("Please provide both start and end dates in format dd/MM/yyyy."));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse(leaveDate.getFromDate(), formatter);
        LocalDate endDate = LocalDate.parse(leaveDate.getToDate(), formatter);

        int workdays = (int) countWorkdays(startDate, endDate);
        Vacation vacation = vacationService.createVacation(userId, workdays, startDate);

        int remainingDays = vacation.getTotalVacationDays() - vacation.getUsedVacationDays();
        String message = String.format(
                "%s %s, your leave request has been successfully registered. You have %d vacation days remaining.",
                vacation.getUser().getFirstName(),
                vacation.getUser().getLastName(),
                remainingDays);

        return ResponseEntity.ok(new GeneralResponse(message));
    }

    private ResponseEntity<GeneralResponse> handleRegisterHours(Long userId, String userInput) {
        int workedHours = nlpService.extractWorkedHours(userInput);
        if (workedHours <= 0) {
            return ResponseEntity.badRequest().body(new GeneralResponse("Please provide a valid number of worked hours."));
        }

        WorkHours workHours = workHoursService.registerWorkHours(userId, workedHours);
        WorkHoursResponse workHoursResponse = workHoursService.getWorkingHours(userId);

        String message = String.format(
                "%s %s, your work hours have been successfully registered. Your total worked hours are now %d.",
                workHours.getUser().getFirstName(),
                workHours.getUser().getLastName(),
                workHoursResponse.getTotalWorkedHours());

        return ResponseEntity.ok(new GeneralResponse(message));
    }

    private ResponseEntity<GeneralResponse> handleGetTotalHours(Long userId) {
        WorkHoursResponse workHoursResponse = workHoursService.getWorkingHours(userId);
        String message = String.format("You have worked a total of %d hours.", workHoursResponse.getTotalWorkedHours());

        return ResponseEntity.ok(new GeneralResponse(message));
    }

    private long countWorkdays(LocalDate start, LocalDate end) {
        return start.datesUntil(end.plusDays(1))
                .filter(date -> date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY)
                .count();
    }
}
