
package com.example.vacation_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "vacations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int vacationYear;
    private int totalVacationDays = 20;
    private int usedVacationDays = 0;

    @Transient
    public int getRemainingVacationDays() {
        return totalVacationDays - usedVacationDays;
    }

}
