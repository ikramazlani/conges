package com.helloIftekhar.springJwt.controller;

import com.helloIftekhar.springJwt.dto.*;
import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.service.AuthenticationService;
import com.helloIftekhar.springJwt.service.DemandeCongeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chef-service/stats")
public class ChefServiceStatsController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DemandeCongeService demandeCongeService;

    // Endpoint pour le nombre total d'utilisateurs dans le service
    @GetMapping("/employees/count")
    public ResponseEntity<Long> getEmployeesCountInService() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User chefService = authenticationService.getUserByUsername(username);

        if (chefService.getDepartement() == null || chefService.getService() == null) {
            return ResponseEntity.badRequest().build();
        }

        List<User> employees = authenticationService.getUsersByDepartementAndService(
                Math.toIntExact(chefService.getDepartement().getId()),
                Math.toIntExact(chefService.getService().getId()));

        return ResponseEntity.ok((long) employees.size());
    }

    // Statistiques des demandes pour un département et service spécifique
    @GetMapping("/stats/{departementId}/{serviceId}")
    public ResponseEntity<Map<String, Long>> getDemandesStatsByService(
            @PathVariable Long departementId,
            @PathVariable Long serviceId) {

        // 1. Récupérer les employés du département et service
        List<User> employees = authenticationService.getUsersByDepartementAndService(
                departementId.intValue(),
                serviceId.intValue());

        // 2. Extraire leurs IDs
        List<Long> employeeIds = employees.stream()
                .map(user -> Long.valueOf(user.getId()))
                .collect(Collectors.toList());

        // 3. Compter les demandes par statut
        long total = demandeCongeService.countByEmployeeIds(employeeIds);
        long approuvees = demandeCongeService.countByEmployeeIdsAndStatus(employeeIds, "APPROUVE");
        long refusees = demandeCongeService.countByEmployeeIdsAndStatus(employeeIds, "REFUSE");
        long enAttente = demandeCongeService.countByEmployeeIdsAndStatus(employeeIds, "EN_ATTENTE");

        return ResponseEntity.ok(Map.of(
                "total", total,
                "approuvees", approuvees,
                "refusees", refusees,
                "enAttente", enAttente
        ));
    }

    // Dernières demandes pour un département et service spécifique
    @GetMapping("/demandes/recentes/{departementId}/{serviceId}")
    public ResponseEntity<List<DemandeCongeResponseDTO>> getRecentDemandesByService(
            @PathVariable Long departementId,
            @PathVariable Long serviceId,
            @RequestParam(defaultValue = "5") int limit) {

        // 1. Récupérer les employés du département et service
        List<User> employees = authenticationService.getUsersByDepartementAndService(
                departementId.intValue(),
                serviceId.intValue());

        // 2. Extraire leurs IDs
        List<Long> employeeIds = employees.stream()
                .map(user -> Long.valueOf(user.getId()))
                .collect(Collectors.toList());

        // 3. Récupérer les dernières demandes
        List<DemandeCongeResponseDTO> recentDemandes =
                demandeCongeService.findRecentByEmployeeIds(employeeIds, limit);

        return ResponseEntity.ok(recentDemandes);
    }

    // Version simplifiée pour le chef connecté
    @GetMapping("/stats/mon-service")
    public ResponseEntity<Map<String, Long>> getMyServiceStats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User chef = authenticationService.getUserByUsername(auth.getName());

        if (chef.getDepartement() == null || chef.getService() == null) {
            return ResponseEntity.badRequest().build();
        }

        return getDemandesStatsByService(
                chef.getDepartement().getId(),
                chef.getService().getId());
    }

    @GetMapping("/demandes/recentes/mon-service")
    public ResponseEntity<List<DemandeCongeResponseDTO>> getMyServiceRecentDemandes(
            @RequestParam(defaultValue = "5") int limit) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User chef = authenticationService.getUserByUsername(auth.getName());

        if (chef.getDepartement() == null || chef.getService() == null) {
            return ResponseEntity.badRequest().build();
        }

        return getRecentDemandesByService(
                chef.getDepartement().getId(),
                chef.getService().getId(),
                limit);
    }




}