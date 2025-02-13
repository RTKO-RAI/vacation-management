package com.example.vacation_management.service;


import com.example.vacation_management.dto.WorkHoursResponse;
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Përdoruesi nuk u gjet me ID: " + userId));

        Project project = user.getProject();
        if (project == null) {
            throw new RuntimeException("Përdoruesi nuk është i lidhur me asnjë projekt.");
        }

        WorkHours workHours = workHoursRepository.findByUserAndWeekStartDate(user, LocalDate.now())
                .orElseGet(() -> {
                    WorkHours newWorkHours = new WorkHours();
                    newWorkHours.setUser(user);
                    newWorkHours.setProject(project);
                    newWorkHours.setWeekStartDate(LocalDate.now());
                    return newWorkHours;
                });

        int newTotalWorkedHours = workHours.getTotalWorkedHours() + workedHoursPerDay;

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
