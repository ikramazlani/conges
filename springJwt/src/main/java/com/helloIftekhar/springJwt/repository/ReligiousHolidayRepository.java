package com.helloIftekhar.springJwt.repository;


import com.helloIftekhar.springJwt.model.ReligiousHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReligiousHolidayRepository extends JpaRepository<ReligiousHoliday, Long> {

    List<ReligiousHoliday> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT h FROM ReligiousHoliday h WHERE YEAR(h.date) = :year")
    List<ReligiousHoliday> findByYear(int year);

    List<ReligiousHoliday> findByIsRecurring(boolean isRecurring);
}