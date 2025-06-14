package com.helloIftekhar.springJwt.dto;


import lombok.Data;

import java.util.Date;
@Data
public class DemandeCongeResponseDTO {
    private Long id;
    private Long idEmployee;
    private Date dateCreation;
    private Date dateDebut;
    private Date dateFin;
    private String motif;
    private String statut;
    private int duree;
    private String remplacerPar;
    private String employeeName;
    private String departement;
    private String role;
    private String serviceName;
    private String motifRefus;
    private String remplacantArab;


    public DemandeCongeResponseDTO() {
    }

    public DemandeCongeResponseDTO(Long id, Long idEmployee, String employeeName, String departement,
                                   Date dateCreation, Date dateDebut, Date dateFin,
                                   String motif, String statut, int duree, String remplacerPar,String role,String serviceName,String remplacantArab) {
        this.id = id;
        this.idEmployee = idEmployee;
        this.employeeName = employeeName;
        this.departement = departement;
        this.dateCreation = dateCreation;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.statut = statut;
        this.duree = duree;
        this.remplacerPar = remplacerPar;
        this.role=role;
        this.serviceName=serviceName;
        this.remplacantArab=remplacantArab;

    }
    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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


}