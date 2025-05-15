package com.helloIftekhar.springJwt.controller;



import com.helloIftekhar.springJwt.model.ReligiousHoliday;
import com.helloIftekhar.springJwt.service.ReligiousHolidayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/holidays/religious")
public class ReligiousHolidayController {

    private final ReligiousHolidayService service;

    public ReligiousHolidayController(ReligiousHolidayService service) {
        this.service = service;
    }

    @GetMapping("/year/{year}")
    public List<ReligiousHoliday> getHolidaysByYear(@PathVariable int year) {
        return service.getHolidaysForYear(year);
    }

    @PostMapping
    public ReligiousHoliday addHoliday(@RequestBody ReligiousHoliday holiday) {
        return service.addHoliday(holiday);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHoliday(@PathVariable Long id) {
        service.deleteHoliday(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recurring")
    public List<ReligiousHoliday> getRecurringHolidays() {
        return service.getAllRecurringHolidays();
    }

    @GetMapping("/range")
    public List<ReligiousHoliday> getHolidaysInRange(
            @RequestParam String start,
            @RequestParam String end) {
        return service.getHolidaysBetweenDates(
                LocalDate.parse(start),
                LocalDate.parse(end));
    }
}