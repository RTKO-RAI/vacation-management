    package com.example.vacation_management.repository;

    import com.example.vacation_management.model.Vacation;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;
    import org.springframework.stereotype.Service;

    @Repository
    public interface VacationRepository extends JpaRepository<Vacation, Long> {
        @Query("SELECT v FROM Vacation v WHERE v.user.id = :userId AND v.vacationYear = :vacationYear")
        Vacation findByUserIdAndVacationYear(@Param("userId") Long userId, @Param("vacationYear") Integer vacationYear);

    }
