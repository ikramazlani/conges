package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.model.ReligiousHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReligiousHolidayRepository extends JpaRepository<ReligiousHoliday, Long> {
    List<ReligiousHoliday> findByStartDateBetween(LocalDate start, LocalDate end);
}