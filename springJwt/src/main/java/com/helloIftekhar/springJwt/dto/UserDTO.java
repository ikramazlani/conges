package com.helloIftekhar.springJwt.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String dateNaissance;
    private String adresse;
    private String grade;
    private String echelle;
    private String username;
    private String matricule;
    private String email;
    private String role;
    private DepartementDTO departement; // Nested DTO

    // Exclude sensitive fields like 'password' and 'tokens'
}