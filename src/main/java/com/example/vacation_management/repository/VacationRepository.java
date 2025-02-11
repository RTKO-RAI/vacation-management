    package com.example.vacation_management.repository;

    import com.example.vacation_management.model.User;
    import com.example.vacation_management.model.Vacation;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;
    import org.springframework.stereotype.Service;

    import java.time.LocalDate;
    import java.util.List;

    @Repository
    public interface VacationRepository extends JpaRepository<Vacation, Long> {
        @Query("SELECT v FROM Vacation v WHERE v.user.id = :userId AND v.vacationYear = :vacationYear")
        List<Vacation> findAllByUserIdAndVacationYear(@Param("userId") Long userId, @Param("vacationYear") Integer vacationYear);

        List<Vacation> findAllByUserIdAndVacationYearOrderByStartDateDesc(Long userId, int vacationYear);

        @Query("SELECT COUNT(v) FROM Vacation v WHERE v.startDate = :startDate AND v.user IN :users")
        long countByStartDateAndUserIn(@Param("startDate") LocalDate startDate, @Param("users") List<User> users);

    }




