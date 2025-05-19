package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.model.ServiceDepartement;
import com.helloIftekhar.springJwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceDepartementRepository extends JpaRepository<ServiceDepartement, Long> {

    List<ServiceDepartement> findByDepartementId(Long departementId);

    boolean existsByNomServiceAndDepartementId(String nomService, Long departementId);
    //  developer par chef service
    @Query("SELECT u FROM User u JOIN FETCH u.service s JOIN FETCH s.departement WHERE s.id = :serviceId AND s.departement.id = :departementId")
    List<User> findByServiceAndDepartement(
            @Param("serviceId") Long serviceId,
            @Param("departementId") Long departementId);
}
