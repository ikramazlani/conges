package com.helloIftekhar.springJwt.dto;


import java.util.Date;

public class DemandeCongeRequestDTO {
    private Date dateDebut;
    private Date dateFin;
    private String motif;
    private int duree;
    private String remplacerPar;
    private long idEmployee;

    // Getters et Setters
    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getRemplacerPar() {
        return remplacerPar;
    }

    public void setRemplacerPar(String remplacerPar) {
        this.remplacerPar = remplacerPar;
    }

    public Long getIdEmployee() {
        return this.idEmployee;
    }


}