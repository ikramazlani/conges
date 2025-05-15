package com.helloIftekhar.springJwt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
@Table(name = "religious_holidays")
public class ReligiousHoliday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int duration; // en jours

    @Column(nullable = false)
    private boolean isRecurring; // si la fête est récurrente chaque année

    // Constructeurs, getters et setters
    public ReligiousHoliday() {}

    public ReligiousHoliday(String name, LocalDate date, int duration, boolean isRecurring) {
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.isRecurring = isRecurring;
    }

    // Getters et setters...
}