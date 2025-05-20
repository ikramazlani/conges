package com.helloIftekhar.springJwt.controller;


import com.helloIftekhar.springJwt.dto.DemandeCongeRequestDTO;
import com.helloIftekhar.springJwt.dto.DemandeCongeResponseDTO;
import com.helloIftekhar.springJwt.dto.DemandeStatsDTO;
import com.helloIftekhar.springJwt.dto.UpdateStatutDTO;
import com.helloIftekhar.springJwt.model.DemandeConge;
import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.repository.DemandeCongeRepository;
import com.helloIftekhar.springJwt.repository.UserRepository;
import com.helloIftekhar.springJwt.service.DemandeCongeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/demandes-conge")
public class DemandeCongeController {


    private final DemandeCongeService demandeCongeService;
    @Autowired
    private DemandeCongeRepository demandeCongeRepository;
    @Autowired
    private UserRepository userRepository;

    public DemandeCongeController(DemandeCongeService demandeCongeService) {
        this.demandeCongeService = demandeCongeService;
    }

    @PostMapping
    public ResponseEntity<DemandeCongeResponseDTO> createDemandeConge(
            @RequestBody DemandeCongeRequestDTO demandeDTO) {
        // Récupérer le username de l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        DemandeCongeResponseDTO responseDTO = demandeCongeService.createDemandeConge(demandeDTO, username);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/historique")
    public ResponseEntity<List<DemandeCongeResponseDTO>> getHistoriqueDemandes() {
        // Récupérer le username de l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<DemandeCongeResponseDTO> historique = demandeCongeService.getHistoriqueDemandes(username);
        return ResponseEntity.ok(historique);
    }



    @GetMapping("/dernieres-demandes")
    public ResponseEntity<List<DemandeCongeResponseDTO>> getDernieresDemandes() {
        // Récupérer le username de l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<DemandeCongeResponseDTO> dernieresDemandes = demandeCongeService.getDernieresDemandes(username, 5);
        return ResponseEntity.ok(dernieresDemandes);
    }


    // Dans DemandeCongeController.java

    @PutMapping("/{id}")
    public ResponseEntity<DemandeCongeResponseDTO> updateDemandeConge(
            @PathVariable Long id,
            @RequestBody DemandeCongeRequestDTO demandeDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        DemandeCongeResponseDTO responseDTO = demandeCongeService.updateDemandeConge(id, demandeDTO, username);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemandeConge(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        demandeCongeService.deleteDemandeConge(id, username);
        return ResponseEntity.noContent().build();
    }

    // Endpoint pour récupérer une demande spécifique
    @GetMapping("/{id}")
    public ResponseEntity<DemandeCongeResponseDTO> getDemandeById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DemandeConge demande = demandeCongeRepository.findByIdAndIdEmployee(id, Long.valueOf(user.getId()))
                .orElseThrow(() -> new RuntimeException("Demande not found or not owned by user"));

        return ResponseEntity.ok(mapToResponseDTO(demande));
    }

    // Vous pouvez ajouter cette méthode utilitaire dans le contrôleur
    private DemandeCongeResponseDTO mapToResponseDTO(DemandeConge demande) {
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

    @GetMapping("/all")
    public ResponseEntity<List<DemandeCongeResponseDTO>> getAllDemandes() {
        List<DemandeCongeResponseDTO> allDemandes = demandeCongeService.getAllDemandes();
        return ResponseEntity.ok(allDemandes);
    }

    @GetMapping("/all-with-employee-info")
    public ResponseEntity<List<DemandeCongeResponseDTO>> getAllDemandesWithEmployeeInfo() {
        List<DemandeCongeResponseDTO> allDemandes = demandeCongeService.getAllDemandesWithEmployeeInfo();
        return ResponseEntity.ok(allDemandes);
    }






    @GetMapping("/stats-globales")
    public ResponseEntity<DemandeStatsDTO> getGlobalStats() {
        DemandeStatsDTO stats = demandeCongeService.getGlobalDemandesStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats-utilisateur")
    public ResponseEntity<DemandeStatsDTO> getUserStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DemandeStatsDTO stats = demandeCongeService.getUserDemandesStats(Long.valueOf(user.getId()));
        return ResponseEntity.ok(stats);
    }




    @PatchMapping("/{id}/statut")
    public ResponseEntity<Void> updateStatutDemande(
            @PathVariable Long id,
            @RequestBody UpdateStatutDTO updateStatutDTO) {

        demandeCongeService.updateStatutDemande(id, updateStatutDTO.getStatut(), updateStatutDTO.getMotifRefus());
        return ResponseEntity.noContent().build();
    }


}