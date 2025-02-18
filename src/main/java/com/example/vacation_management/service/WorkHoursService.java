package com.example.vacation_management.service;


import com.example.vacation_management.dto.WorkHoursResponse;
import com.example.vacation_management.exception.WorkingHoursException;
import com.example.vacation_management.model.Project;
import com.example.vacation_management.model.User;
import com.example.vacation_management.model.WorkHours;
import com.example.vacation_management.repository.UserRepository;
import com.example.vacation_management.repository.WorkHoursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WorkHoursService {

    private final WorkHoursRepository workHoursRepository;
    private final UserRepository userRepository;


    public WorkHours registerWorkHours(Long userId, int workedHoursPerDay) {
        if (workedHoursPerDay > 8) {
            throw new WorkingHoursException("Nuk mund të regjistroni më shumë se 8 orë në ditë.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Përdoruesi nuk u gjet me ID: " + userId));

        Project project = user.getProject();
        if (project == null) {
            throw new WorkingHoursException("Përdoruesi nuk është i lidhur me asnjë projekt.");
        }

        LocalDate today = LocalDate.now();

        if (today.getDayOfWeek().getValue() > 5) {
            throw new WorkingHoursException("Nuk mund të regjistroni orë pune gjatë fundjavës.");
        }

        WorkHours workHours = workHoursRepository.findByUserAndWeekStartDate(user, today)
                .orElseGet(() -> {
                    WorkHours newWorkHours = new WorkHours();
                    newWorkHours.setUser(user);
                    newWorkHours.setProject(project);
                    newWorkHours.setWeekStartDate(today);
                    return newWorkHours;
                });

        int newTotalWorkedHours = workHours.getTotalWorkedHours() + workedHoursPerDay;

        if (newTotalWorkedHours > 8) {
            throw new RuntimeException("Nuk mund të regjistroni më shumë se 8 orë pune për këtë ditë.");
        }
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        long workingDaysThisMonth = today.withDayOfMonth(1)
                .datesUntil(today.withDayOfMonth(today.lengthOfMonth()))
                .filter(date -> date.getDayOfWeek().getValue() <= 5)
                .count();

        int totalWorkedThisMonth = workHoursRepository
                .findTotalWorkedHoursByUserAndMonth(user, currentMonth, currentYear);

        if ((totalWorkedThisMonth + workedHoursPerDay) > (workingDaysThisMonth * 8)) {
            throw new RuntimeException("Nuk mund të regjistroni më shumë orë se sa ditët e punës në muaj.");
        }

        workHours.setTotalWorkedHours(newTotalWorkedHours);

        return workHoursRepository.save(workHours);
    }



    public WorkHoursResponse getWorkingHours(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Përdoruesi nuk u gjet me ID: " + userId));


        WorkHours workHours = workHoursRepository.findByUserAndWeekStartDate(user, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Nuk janë regjistruar orë pune për këtë javë për përdoruesin me ID: " + userId));

        return new WorkHoursResponse(userId, workHours.getTotalWorkedHours(), workHours.getWeekStartDate());
    }

}
