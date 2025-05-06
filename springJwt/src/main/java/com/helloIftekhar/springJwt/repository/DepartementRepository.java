package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartementRepository extends JpaRepository<Departement, Long> {
    // Méthodes personnalisées si nécessaire
    boolean existsByNomDepartement(String nomDepartement);


}