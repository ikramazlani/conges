package com.helloIftekhar.springJwt.service;

import com.helloIftekhar.springJwt.dto.DemandeCongeRequestDTO;
import com.helloIftekhar.springJwt.dto.DemandeCongeResponseDTO;
import com.helloIftekhar.springJwt.dto.ServiceDepartementDTO;
import com.helloIftekhar.springJwt.dto.ServiceDepartementMapper;
import com.helloIftekhar.springJwt.model.DemandeConge;
import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.repository.DemandeCongeRepository;
import com.helloIftekhar.springJwt.repository.DepartementRepository;
import com.helloIftekhar.springJwt.repository.ServiceDepartementRepository;
import com.helloIftekhar.springJwt.repository.UserRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class DemandeCongeService {

    @Autowired
    public ServiceDepartementRepository serviceDepartementRepository;
    private final DemandeCongeRepository demandeCongeRepository;
    private final UserRepository userRepository;


    public DemandeCongeService(DemandeCongeRepository demandeCongeRepository,
                               UserRepository userRepository) {
        this.demandeCongeRepository = demandeCongeRepository;
        this.userRepository = userRepository;
    }

    public DemandeCongeResponseDTO createDemandeConge(DemandeCongeRequestDTO demandeDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DemandeConge demande = new DemandeConge();
        demande.setIdEmployee(Long.valueOf(user.getId()));
        demande.setDateDebut(demandeDTO.getDateDebut());
        demande.setDateFin(demandeDTO.getDateFin());
        demande.setMotif(demandeDTO.getMotif());
        demande.setDuree(demandeDTO.getDuree());
        demande.setRemplacerPar(demandeDTO.getRemplacerPar());
        demande.setStatut("EN_ATTENTE"); // Set default status

        DemandeConge savedDemande = demandeCongeRepository.save(demande);
        return mapToResponseDTO(savedDemande);
    }

    private DemandeCongeResponseDTO mapToResponseDTO(DemandeConge demande) {
        // If you don't have a builder pattern in your DTO, use regular constructor/setter approach
        DemandeCongeResponseDTO responseDTO = new DemandeCongeResponseDTO();
        responseDTO.setId(demande.getId());
        responseDTO.setIdEmployee(demande.getIdEmployee());
        responseDTO.setDateCreation(demande.getDateCreation());
        responseDTO.setDateDebut(demande.getDateDebut());
        responseDTO.setDateFin(demande.getDateFin());
        responseDTO.setMotif(demande.getMotif());
        responseDTO.setStatut(demande.getStatut());
        responseDTO.setDuree(demande.getDuree());
        responseDTO.setRemplacerPar(demande.getRemplacerPar());

        return responseDTO;
    }

    public List<DemandeCongeResponseDTO> getHistoriqueDemandes(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<DemandeConge> demandes = demandeCongeRepository.findByIdEmployeeOrderByDateCreationDesc(Long.valueOf(user.getId()));

        return demandes.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getDemandesStats(Long userId) {
        List<DemandeCongeRepository.StatusCount> counts = demandeCongeRepository.countByStatus(userId);

        Map<String, Long> stats = new HashMap<>();
        stats.put("approuvees", 0L);
        stats.put("refusees", 0L);
        stats.put("enAttente", 0L);

        counts.forEach(sc -> {
            String status = sc.getStatus().toUpperCase();
            if (status.equals("APPROUVÉ") || status.equals("APPROUVE")) {
                stats.put("approuvees", sc.getCount());
            } else if (status.equals("REFUSÉ") || status.equals("REFUSE")) {
                stats.put("refusees", sc.getCount());
            } else if (status.equals("EN_ATTENTE") || status.equals("EN ATTENTE")) {
                stats.put("enAttente", sc.getCount());
            }
        });

        return stats;
    }

    public List<DemandeCongeResponseDTO> getDernieresDemandes(String username, int limit) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(0, limit, Sort.by("dateCreation").descending());
        Page<DemandeConge> demandesPage = demandeCongeRepository
                .findByIdEmployeeOrderByDateCreationDesc(Long.valueOf(user.getId()), pageable);

        return demandesPage.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }





    // Dans DemandeCongeService.java

    // Méthode pour mettre à jour une demande
    public DemandeCongeResponseDTO updateDemandeConge(Long demandeId, DemandeCongeRequestDTO demandeDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DemandeConge demande = demandeCongeRepository.findByIdAndIdEmployee(demandeId, Long.valueOf(user.getId()))
                .orElseThrow(() -> new RuntimeException("Demande not found or not owned by user"));

        // Vérifier si la demande peut être modifiée (statut EN_ATTENTE)
        if (!"EN_ATTENTE".equals(demande.getStatut())) {
            throw new RuntimeException("Seules les demandes en attente peuvent être modifiées");
        }

        demande.setDateDebut(demandeDTO.getDateDebut());
        demande.setDateFin(demandeDTO.getDateFin());
        demande.setMotif(demandeDTO.getMotif());
        demande.setDuree(demandeDTO.getDuree());
        demande.setRemplacerPar(demandeDTO.getRemplacerPar());

        DemandeConge updatedDemande = demandeCongeRepository.save(demande);
        return mapToResponseDTO(updatedDemande);
    }

    // Méthode pour supprimer une demande
    public void deleteDemandeConge(Long demandeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DemandeConge demande = demandeCongeRepository.findByIdAndIdEmployee(demandeId, Long.valueOf(user.getId()))
                .orElseThrow(() -> new RuntimeException("Demande not found or not owned by user"));

        // Vérifier si la demande peut être supprimée (statut EN_ATTENTE)
        if (!"EN_ATTENTE".equals(demande.getStatut())) {
            throw new RuntimeException("Seules les demandes en attente peuvent être supprimées");
        }

        demandeCongeRepository.delete(demande);
    }

    public List<ServiceDepartementDTO> getAllServicesByDepartement(Long departementId) {
        return serviceDepartementRepository.findByDepartementId(departementId)
                .stream()
                .map(ServiceDepartementMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<DemandeCongeResponseDTO> getAllDemandes() {
        List<DemandeConge> demandes = demandeCongeRepository.findAll();
        return demandes.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<DemandeCongeResponseDTO> getAllDemandesWithEmployeeInfo() {
        return demandeCongeRepository.findAllWithEmployeeInfo();
    }


}