package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);


    //add chef service codes


    List<User> findByDepartementIdAndServiceId(Integer departementId, Integer serviceId);


    List<User> findByRole(String role);

    // Trouver par matricule
    Optional<User> findByMatricule(String matricule);


    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Trouver par département
    List<User> findByDepartementId(Integer departementId);


    // Vérifier si un username existe
    boolean existsByUsername(String username);

    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    Optional<Long> findIdByUsername(@Param("username") String username);


    @Query("SELECT COUNT(u) FROM User u WHERE u.service.id = :serviceId")
    Long countByServiceId(@Param("serviceId") Long serviceId);
    // developer par  chef service
    @Query("SELECT u FROM User u WHERE u.service.id = :serviceId AND u.service.departement.id = :departementId")
    List<User> findByServiceAndDepartement(
            @Param("serviceId") Long serviceId,
            @Param("departementId") Long departementId);

}

