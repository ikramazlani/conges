package com.helloIftekhar.springJwt.dto;


import lombok.Data;

@Data
public class SoldeCongeDTO {
    private Long id;
    private int soldeGlobal;
    private int soldeAnneeCourante;
    private int soldeAnneePrecedente;
    private int annee;

    // Constructeurs
    public SoldeCongeDTO() {}

    public SoldeCongeDTO(Long id, int soldeGlobal, int soldeAnneeCourante,
                         int soldeAnneePrecedente, int annee) {
        this.id = id;
        this.soldeGlobal = soldeGlobal;
        this.soldeAnneeCourante = soldeAnneeCourante;
        this.soldeAnneePrecedente = soldeAnneePrecedente;
        this.annee = annee;
    }


}