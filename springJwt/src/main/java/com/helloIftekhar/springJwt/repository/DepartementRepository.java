package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartementRepository extends JpaRepository<Departement, Long> {
    // Méthodes personnalisées si nécessaire
    boolean existsByNomDepartement(String nomDepartement);
    List<Departement> findAllByOrderByNomDepartementAsc();

}