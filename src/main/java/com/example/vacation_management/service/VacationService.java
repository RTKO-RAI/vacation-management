package com.example.vacation_management.service;

import com.example.vacation_management.dto.VacationResponse;
import com.example.vacation_management.exception.ResourceNotFoundException;
import com.example.vacation_management.exception.VacationException;
import com.example.vacation_management.model.Project;
import com.example.vacation_management.model.User;
import com.example.vacation_management.model.Vacation;
import com.example.vacation_management.repository.ProjectRepository;
import com.example.vacation_management.repository.UserRepository;
import com.example.vacation_management.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationService {

    private final VacationRepository vacationRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public Vacation createVacation(Long userId, int vacationYear, int usedDays, LocalDate startDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Përdoruesi nuk u gjet!"));

        Project project = user.getProject();
        if (project == null) {
            throw new VacationException("Përdoruesi nuk është i lidhur me asnjë projekt.");
        }

        List<User> projectMembers = project.getUsers();
        if (projectMembers.isEmpty()) {
            throw new VacationException("Projekti nuk ka anëtarë.");
        }

        long membersOnLeave = vacationRepository.countByStartDateAndUserIn(startDate, projectMembers);

        int maxAllowedOnLeave = projectMembers.size() / 2;
        if (membersOnLeave > maxAllowedOnLeave) {
            throw new VacationException("Nuk mund të merrni pushim, pasi më shumë se 50% e ekipit janë në pushim për këtë datë.");
        }

        List<Vacation> existingVacations = vacationRepository.findAllByUserIdAndVacationYear(userId, vacationYear);

        int totalUsedDays = existingVacations.stream().mapToInt(Vacation::getUsedVacationDays).sum();
        int totalRemainingDays = 20 - totalUsedDays;

        if (usedDays > totalRemainingDays) {
            throw new VacationException("Nuk mund të marrësh më shumë se " + totalRemainingDays + " ditë pushimi.");
        }

        Vacation newVacation = new Vacation();
        newVacation.setUser(user);
        newVacation.setVacationYear(vacationYear);
        newVacation.setUsedVacationDays(usedDays);
        newVacation.setTotalVacationDays(totalRemainingDays);
        newVacation.setStartDate(startDate);
        return  vacationRepository.save(newVacation);
    }


    public VacationResponse getRemainingVacationDays(Long userId, int year) {
        List<Vacation> vacations = vacationRepository.findAllByUserIdAndVacationYear(userId, year);

        if (vacations.isEmpty()) {
            throw new ResourceNotFoundException("Nuk u gjetën pushime për këtë përdorues në vitin " + year + ".");
        }

        int totalUsedDays = vacations.stream().mapToInt(Vacation::getUsedVacationDays).sum();
        int remainingDays = 20 - totalUsedDays;

        VacationResponse response = new VacationResponse(
                vacations.get(0).getUser().getFirstName(),
                vacations.get(0).getUser().getLastName(),
                remainingDays
        );
        return response;
    }


    public Vacation updateVacation(Long vacationId, Vacation vacationDetails) {
        Vacation existingVacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new ResourceNotFoundException("Pushimet nuk u gjetën me ID: " + vacationId));

        existingVacation.setVacationYear(vacationDetails.getVacationYear());
        existingVacation.setTotalVacationDays(vacationDetails.getTotalVacationDays());
        existingVacation.setUsedVacationDays(vacationDetails.getUsedVacationDays());

        return vacationRepository.save(existingVacation);
    }

    public void deleteVacation(Long vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new ResourceNotFoundException("Pushimet nuk u gjetën me ID: " + vacationId));

        vacationRepository.delete(vacation);
    }
}
