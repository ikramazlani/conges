package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.dto.DemandeCongeResponseDTO;
import com.helloIftekhar.springJwt.model.DemandeConge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DemandeCongeRepository extends JpaRepository<DemandeConge, Long> {

    List<DemandeConge> findByIdEmployee(Long idEmployee);



    @Modifying
    @Query("DELETE FROM DemandeConge d WHERE d.idEmployee = :employeeId")
    void deleteByIdEmployee(@Param("employeeId") Long employeeId);

    Page<DemandeConge> findByIdEmployeeOrderByDateCreationDesc(Long idEmployee, Pageable pageable);

    // Méthode pour obtenir toute la liste (sans pagination)
    List<DemandeConge> findByIdEmployeeOrderByDateCreationDesc(Long idEmployee);
    @Query("SELECT d FROM DemandeConge d WHERE d.idEmployee = :employeeId ORDER BY d.dateCreation DESC")
    Page<DemandeConge> findByEmployeeSorted(@Param("employeeId") Long employeeId, Pageable pageable);

    @Query("SELECT d.statut as status, COUNT(d) as count " +
            "FROM DemandeConge d WHERE d.idEmployee = :userId " +
            "GROUP BY d.statut")
    List<StatusCount> countByStatus(@Param("userId") Long userId);

    interface StatusCount {
        String getStatus();
        Long getCount();
    }


    @Query("SELECT new com.helloIftekhar.springJwt.dto.DemandeCongeResponseDTO(" +
            "d.id, d.idEmployee, CONCAT(u.firstName, ' ', u.lastName), dep.nomDepartement, " +
            "d.dateCreation, d.dateDebut, d.dateFin, d.motif, d.statut, d.duree, d.remplacerPar) " +
            "FROM DemandeConge d " +
            "JOIN User u ON d.idEmployee = u.id " +
            "LEFT JOIN u.departement dep")
    List<DemandeCongeResponseDTO> findAllWithEmployeeInfo();

    Optional<DemandeConge> findByIdAndIdEmployee(Long id, Long idEmployee);

    // Dans DemandeCongeRepository.java
    @Modifying
    @Query("UPDATE DemandeConge d SET d.dateDebut = :dateDebut, d.dateFin = :dateFin, d.motif = :motif, d.duree = :duree, d.remplacerPar = :remplacerPar WHERE d.id = :id AND d.idEmployee = :employeeId")
    int updateDemande(@Param("id") Long id,
                      @Param("employeeId") Long employeeId,
                      @Param("dateDebut") Date dateDebut,
                      @Param("dateFin") Date dateFin,
                      @Param("motif") String motif,
                      @Param("duree") int duree,
                      @Param("remplacerPar") String remplacerPar);
}

