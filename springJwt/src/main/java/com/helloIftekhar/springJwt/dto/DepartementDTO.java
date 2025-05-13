package com.helloIftekhar.springJwt.dto;

import lombok.Data;
import java.util.Date;

@Data
public class DepartementDTO {
    private Long id;
    private String nomDepartement;
    private String description;
    private Date date;
    private Integer nombreUtilisateurs;
}