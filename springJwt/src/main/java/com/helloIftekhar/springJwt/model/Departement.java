package com.helloIftekhar.springJwt.model;


import jakarta.persistence.*;


import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "departements")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_departement")
    private String nomDepartement;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    @OneToMany(mappedBy = "departement", fetch = FetchType.EAGER)
    private List<User> users;

    // Constructeur avec paramètres
    public Departement(String nomDepartement, String description, Date date) {
        this.nomDepartement = nomDepartement;
        this.description = description;
        this.date = date;
    }

    // Constructeur par défaut requis par JPA
    public Departement() {
    }

    public String getNom() {
        return this.nomDepartement;
    }
}