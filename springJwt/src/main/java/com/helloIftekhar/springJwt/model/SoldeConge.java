package com.helloIftekhar.springJwt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Year;
import java.util.Date;

@Entity
@Table(name = "solde_conges",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_employee", "annee"}, name = "unique_employee_annee"))
public class SoldeConge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_employee", nullable = false)
    @JsonIgnore
    private User employee;



    @Column(name = "solde_global", columnDefinition = "integer default 22")
    private int soldeGlobal;

    @Column(name = "solde_annee_courante", columnDefinition = "integer default 22")
    private int soldeAnneeCourante;

    @Column(name = "solde_annee_precedente", columnDefinition = "integer default 0")
    private int soldeAnneePrecedente;

    @Column(name = "annee")
    private  Integer annee;

    @Column(name = "date_mise_a_jour", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)

    private Date dateMiseAJour;

    // Constructeurs
    public SoldeConge() {
    }

    public SoldeConge(User employee, int soldeGlobal, int soldeAnneeCourante,
                      int soldeAnneePrecedente, Integer  annee) {
        this.employee = employee;
        this.soldeGlobal = soldeGlobal;
        this.soldeAnneeCourante = soldeAnneeCourante;
        this.soldeAnneePrecedente = soldeAnneePrecedente;
        this.annee = annee;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public int getSoldeGlobal() {
        return soldeGlobal;
    }

    public void setSoldeGlobal(int soldeGlobal) {
        this.soldeGlobal = soldeGlobal;
    }

    public int getSoldeAnneeCourante() {
        return soldeAnneeCourante;
    }

    public void setSoldeAnneeCourante(int soldeAnneeCourante) {
        this.soldeAnneeCourante = soldeAnneeCourante;
    }

    public int getSoldeAnneePrecedente() {
        return soldeAnneePrecedente;
    }

    public void setSoldeAnneePrecedente(int soldeAnneePrecedente) {
        this.soldeAnneePrecedente = soldeAnneePrecedente;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Date getDateMiseAJour() {
        return dateMiseAJour;
    }

    public void setDateMiseAJour(Date dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }
}