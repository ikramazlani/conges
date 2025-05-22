package com.helloIftekhar.springJwt.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dateNaissance")
    private String dateNaissance;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "grade")
    private String grade;

    @Column(name = "echelle")
    private String echelle;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;


    @Column(name = "matricule")
    private String matricule;

    @Column(name = "email")
    private String email;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departement_id", nullable = true)
    private Departement departement;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id", nullable = true)
    private ServiceDepartement service;

    //autres chmaps arba

    @Column(name = "nom_arab")
    private String nomArab;

    @Column(name = "prenom_arab")
    private String prenomArab;

    @Column(name = "grade_arab")
    private String gradeArab;

    @Column(name = "echelle_arab")
    private String echelleArab;



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens;

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Departement getDepartement() {
        return departement;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole()));
    }


}
