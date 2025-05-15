package com.helloIftekhar.springJwt.service;



import com.helloIftekhar.springJwt.model.ReligiousHoliday;
import com.helloIftekhar.springJwt.repository.ReligiousHolidayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReligiousHolidayService {

    private final ReligiousHolidayRepository repository;

    public ReligiousHolidayService(ReligiousHolidayRepository repository) {
        this.repository = repository;
    }

    public List<ReligiousHoliday> getHolidaysForYear(int year) {
        return repository.findByYear(year);
    }

    public ReligiousHoliday addHoliday(ReligiousHoliday holiday) {
        return repository.save(holiday);
    }

    public void deleteHoliday(Long id) {
        repository.deleteById(id);
    }

    public List<ReligiousHoliday> getAllRecurringHolidays() {
        return repository.findByIsRecurring(true);
    }

    public List<ReligiousHoliday> getHolidaysBetweenDates(LocalDate start, LocalDate end) {
        return repository.findByDateBetween(start, end);
    }
}