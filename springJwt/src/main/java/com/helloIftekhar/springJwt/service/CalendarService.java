package com.helloIftekhar.springJwt.service;


import com.helloIftekhar.springJwt.dto.ReligiousHolidayDTO;
import com.helloIftekhar.springJwt.model.ReligiousHoliday;
import com.helloIftekhar.springJwt.repository.ReligiousHolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalendarService {
    @Autowired
    private ReligiousHolidayRepository religiousHolidayRepository;

    // Fêtes nationales fixes du Maroc (à adapter selon vos besoins)
    private static final Map<String, LocalDate> NATIONAL_HOLIDAYS = Map.of(
            "Nouvel An", LocalDate.of(LocalDate.now().getYear(), 1, 1),
            "Manifeste de l'Indépendance", LocalDate.of(LocalDate.now().getYear(), 1, 11),
            "Fête du Travail", LocalDate.of(LocalDate.now().getYear(), 5, 1),
            "Fête du Trône", LocalDate.of(LocalDate.now().getYear(), 7, 30),
            "Fête de l'Oued Ed-Dahab", LocalDate.of(LocalDate.now().getYear(), 8, 14),
            "Révolution du Roi et du Peuple", LocalDate.of(LocalDate.now().getYear(), 8, 20),
            "Fête de la Jeunesse", LocalDate.of(LocalDate.now().getYear(), 8, 21),
            "Anniversaire de la Marche Verte", LocalDate.of(LocalDate.now().getYear(), 11, 6),
            "Fête de l'Indépendance", LocalDate.of(LocalDate.now().getYear(), 11, 18)

    );

    public Map<String, Object> getCalendarData(int year) {
        Map<String, Object> calendarData = new HashMap<>();

        // Fêtes nationales
        calendarData.put("nationalHolidays", NATIONAL_HOLIDAYS);

        // Fêtes religieuses (récupérées de la base de données)
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        List<ReligiousHoliday> religiousHolidays = religiousHolidayRepository.findByStartDateBetween(start, end);
        calendarData.put("religiousHolidays", religiousHolidays);

        return calendarData;
    }

    public ReligiousHoliday addReligiousHoliday(ReligiousHolidayDTO dto) {
        ReligiousHoliday holiday = new ReligiousHoliday();
        holiday.setName(dto.getName());
        holiday.setStartDate(dto.getStartDate());
        holiday.setEndDate(dto.getEndDate());
        return religiousHolidayRepository.save(holiday);
    }


    public ReligiousHoliday updateReligiousHoliday(Long id, ReligiousHolidayDTO dto) {
        ReligiousHoliday holiday = religiousHolidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jour férié non trouvé avec l'id : " + id));
        holiday.setName(dto.getName());
        holiday.setStartDate(dto.getStartDate());
        holiday.setEndDate(dto.getEndDate());
        return religiousHolidayRepository.save(holiday);
    }

    public void deleteReligiousHoliday(Long id) {
        if (!religiousHolidayRepository.existsById(id)) {
            throw new RuntimeException("Jour férié non trouvé avec l'id : " + id);
        }
        religiousHolidayRepository.deleteById(id);
    }

}