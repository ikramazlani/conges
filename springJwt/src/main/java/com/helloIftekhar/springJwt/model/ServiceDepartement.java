package com.helloIftekhar.springJwt.model;

import jakarta.persistence.*;

@Entity
@Table(name = "services_departement")
public class ServiceDepartement {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "nom_service", nullable = false, length = 100)
        private String nomService;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "departement_id", nullable = false)
        private Departement departement;

        // Constructeurs
        public ServiceDepartement() {
        }

        public ServiceDepartement(String nomService, Departement departement) {
            this.nomService = nomService;
            this.departement = departement;
        }

        // Getters et Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNomService() {
            return nomService;
        }

        public void setNomService(String nomService) {
            this.nomService = nomService;
        }

        public Departement getDepartement() {
            return departement;
        }

        public void setDepartement(Departement departement) {
            this.departement = departement;
        }

}
