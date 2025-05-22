package com.helloIftekhar.springJwt.repository;

import com.helloIftekhar.springJwt.dto.DemandeCongeResponseDTO;
import com.helloIftekhar.springJwt.dto.StatutDemande;
import com.helloIftekhar.springJwt.model.DemandeConge;
import com.helloIftekhar.springJwt.model.User;
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




    //affihcer les departement de chef de service




    // Pour les stats globales
    @Query("SELECT COUNT(d) FROM DemandeConge d WHERE d.statut = 'APPROUVE' OR d.statut = 'APPROUVÉ'")
    long countApprouvees();

    @Query("SELECT COUNT(d) FROM DemandeConge d WHERE d.statut = 'REFUSE' OR d.statut = 'REFUSÉ'")
    long countRefusees();

    @Query("SELECT COUNT(d) FROM DemandeConge d WHERE d.statut = 'EN_ATTENTE' OR d.statut = 'EN ATTENTE'")
    long countEnAttente();

    // Pour les stats par utilisateur
    @Query("SELECT COUNT(d) FROM DemandeConge d WHERE (d.statut = 'APPROUVE' OR d.statut = 'APPROUVÉ') AND d.idEmployee = :userId")
    long countApprouveesByUser(Long userId);

    @Query("SELECT COUNT(d) FROM DemandeConge d WHERE (d.statut = 'REFUSE' OR d.statut = 'REFUSÉ') AND d.idEmployee = :userId")
    long countRefuseesByUser(Long userId);

    @Query("SELECT COUNT(d) FROM DemandeConge d WHERE (d.statut = 'EN_ATTENTE' OR d.statut = 'EN ATTENTE') AND d.idEmployee = :userId")
    long countEnAttenteByUser(Long userId);



    @Modifying
    @Query("UPDATE DemandeConge d SET d.statut = :statut, d.motifRefus = :motifRefus WHERE d.id = :id")
    void updateStatutAndMotifRefus(@Param("id") Long id,
                                   @Param("statut") String statut,
                                   @Param("motifRefus") String motifRefus);


    @Query("SELECT new com.helloIftekhar.springJwt.dto.DemandeCongeResponseDTO(" +
            "d.id, d.idEmployee, CONCAT(u.firstName, ' ', u.lastName), dep.nomDepartement, " +
            "d.dateCreation, d.dateDebut, d.dateFin, d.motif, d.statut, d.duree, d.remplacerPar, u.role, s.nomService) " +
            "FROM DemandeConge d " +
            "JOIN User u ON d.idEmployee = u.id " +
            "LEFT JOIN u.departement dep " +
            "LEFT JOIN u.service s")
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


/// ////////////////////////////
@Query("SELECT d FROM DemandeConge d WHERE d.idEmployee IN :employeeIds ORDER BY d.dateCreation DESC")
List<DemandeConge> findByIdEmployeeIn(@Param("employeeIds") List<Long> employeeIds);


}

