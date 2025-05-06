package com.helloIftekhar.springJwt.dto;

import com.helloIftekhar.springJwt.model.Role;
import lombok.Data;


import java.util.List;

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
    private Role role;
    private DepartementDTO departement; // Nested DTO

    // Exclude sensitive fields like 'password' and 'tokens'
}