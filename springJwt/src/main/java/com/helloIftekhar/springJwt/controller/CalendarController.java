package com.helloIftekhar.springJwt.controller;


import com.helloIftekhar.springJwt.dto.ReligiousHolidayDTO;
import com.helloIftekhar.springJwt.model.ReligiousHoliday;
import com.helloIftekhar.springJwt.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    @Autowired
    private CalendarService calendarService;

    @GetMapping("/{year}")
    public ResponseEntity<Map<String, Object>> getCalendar(@PathVariable int year) {
        return ResponseEntity.ok(calendarService.getCalendarData(year));
    }

    @PostMapping("/religious-holidays")
    public ResponseEntity<ReligiousHoliday> addReligiousHoliday(@RequestBody ReligiousHolidayDTO dto) {
        return ResponseEntity.ok(calendarService.addReligiousHoliday(dto));
    }



    @PutMapping("/religious-holidays/{id}")
    public ResponseEntity<ReligiousHoliday> updateReligiousHoliday(@PathVariable Long id, @RequestBody ReligiousHolidayDTO dto) {
        return ResponseEntity.ok(calendarService.updateReligiousHoliday(id, dto));
    }



    @DeleteMapping("/religious-holidays/{id}")
    public ResponseEntity<Void> deleteReligiousHoliday(@PathVariable Long id) {
        calendarService.deleteReligiousHoliday(id);
        return ResponseEntity.noContent().build();
    }

}