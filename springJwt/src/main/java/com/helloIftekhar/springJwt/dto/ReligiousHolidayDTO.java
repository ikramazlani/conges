package com.helloIftekhar.springJwt.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class ReligiousHolidayDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    // Getters and Setters
}