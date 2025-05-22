package com.helloIftekhar.springJwt.dto;

import com.helloIftekhar.springJwt.model.User;
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

    // chef service
    private Long serviceId;
    private String serviceName;
    private Long departementId;
    private String departementName;

    //des cchmaps pour arab
    private String nomArab;
    private String prenomArab;
    private String gradeArab;
    private String echelleArab;
    }


