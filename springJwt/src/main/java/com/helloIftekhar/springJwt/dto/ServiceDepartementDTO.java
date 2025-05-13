package com.helloIftekhar.springJwt.dto;

import lombok.Data;

@Data
public class ServiceDepartementDTO {
    private Long id;
    private String nomService;
    private Long departementId;
    private String nomDepartement; // Optionnel pour l'affichage
}