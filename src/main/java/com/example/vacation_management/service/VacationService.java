package com.example.vacation_management.service;

import com.example.vacation_management.dto.VacationResponse;
import com.example.vacation_management.exception.ResourceNotFoundException;
import com.example.vacation_management.model.User;
import com.example.vacation_management.model.Vacation;
import com.example.vacation_management.repository.UserRepository;
import com.example.vacation_management.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VacationService {

    private final VacationRepository vacationRepository;
    private final UserRepository userRepository;

    public Vacation createVacation(Long userId, int vacationYear, int usedDays) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Përdoruesi nuk u gjet!"));

        Vacation existingVacation = vacationRepository.findByUserIdAndVacationYear(userId, vacationYear);

        if (existingVacation != null) {
            int totalUsedDaysForYear = existingVacation.getUsedVacationDays() + usedDays;
            if (totalUsedDaysForYear > 20) {
                throw new IllegalArgumentException("Shuma totale e ditëve të pushimit për vitin " + vacationYear + " nuk mund të kalojë 20.");
            }
            existingVacation.setUsedVacationDays(totalUsedDaysForYear);
            return vacationRepository.save(existingVacation);
        } else {
            if (usedDays > 20) {
                throw new IllegalArgumentException("Ditët e pushimit për vitin " + vacationYear + " nuk mund të jenë më shumë se 20.");
            }

            Vacation newVacation = new Vacation();
            newVacation.setUser(user);
            newVacation.setVacationYear(vacationYear);
            newVacation.setUsedVacationDays(usedDays);
            newVacation.setTotalVacationDays(20);

            return vacationRepository.save(newVacation);
        }
    }

    public VacationResponse getRemainingVacationDays(Long userId, int year) {
        Vacation vacation = vacationRepository.findByUserIdAndVacationYear(userId, year);

        if (vacation != null) {
            return new VacationResponse(
                    vacation.getUser().getFirstName(),
                    vacation.getUser().getLastName(),
                    vacation.getRemainingVacationDays()
            );
        } else {
            throw new ResourceNotFoundException("Nuk u gjetën pushime për përdoruesin me ID " + userId + " për vitin " + year + ".");
        }
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
