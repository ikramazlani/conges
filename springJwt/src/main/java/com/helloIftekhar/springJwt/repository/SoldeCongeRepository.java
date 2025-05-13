package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.dto.SoldeCongeDTO;
import com.helloIftekhar.springJwt.model.SoldeConge;
import com.helloIftekhar.springJwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface SoldeCongeRepository extends JpaRepository<SoldeConge, Long> {
    @Query("SELECT NEW com.helloIftekhar.springJwt.dto.SoldeCongeDTO(" +
            "s.id, s.soldeGlobal, s.soldeAnneeCourante, s.soldeAnneePrecedente, s.annee) " +
            "FROM SoldeConge s WHERE s.employee.id = :userId AND s.annee = :year")
    Optional<SoldeCongeDTO> findSoldeByUserIdAndYear(@Param("userId") Long userId,
                                                     @Param("year") Integer year);

    @Query("SELECT NEW com.helloIftekhar.springJwt.dto.SoldeCongeDTO(" +
            "s.id, s.soldeGlobal, s.soldeAnneeCourante, s.soldeAnneePrecedente, s.annee) " +
            "FROM SoldeConge s WHERE s.employee.id = :userId ORDER BY s.annee DESC LIMIT 1")
    Optional<SoldeCongeDTO> findLatestSoldeByUserId(@Param("userId") Long userId);



    @Modifying
    @Query("DELETE FROM SoldeConge s WHERE s.employee.id = :employeeId")
    void deleteByEmployeeId(@Param("employeeId") Long employeeId);


    @Query("SELECT d.statut as status, COUNT(d) as count " +
            "FROM DemandeConge d WHERE d.idEmployee = :userId " +
            "GROUP BY d.statut")
    List<StatusCount> countByStatus(@Param("userId") Long userId);

    public interface StatusCount {
        String getStatus();
        Long getCount();
    }
}