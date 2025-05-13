package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.model.ServiceDepartement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceDepartementRepository extends JpaRepository<ServiceDepartement, Long> {

    List<ServiceDepartement> findByDepartementId(Long departementId);

    boolean existsByNomServiceAndDepartementId(String nomService, Long departementId);
}
