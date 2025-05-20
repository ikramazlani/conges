package com.helloIftekhar.springJwt.controller;

import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chef-service")
public class ChefServiceController {

    @Autowired
    private AuthenticationService authenticationService;

    // Nouvelle endpoint paramétrée
    @GetMapping("/{departementId}/{serviceId}/collegues")
    public ResponseEntity<List<User>> getColleaguesByDepartmentAndService(
            @PathVariable Integer departementId,
            @PathVariable Integer serviceId) {

        // Récupérer l'utilisateur connecté pour vérification des droits si nécessaire
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = authenticationService.getUserByUsername(username);

        // Récupérer les utilisateurs du département et service spécifiés
        List<User> colleagues = authenticationService.getUsersByDepartementAndService(departementId, serviceId)
                .stream()
                .filter(user -> !user.getId().equals(currentUser.getId())) // Exclure l'utilisateur courant
                .toList();

        return ResponseEntity.ok(colleagues);
    }

    // Ancienne endpoint (peut être gardée pour compatibilité)
    @GetMapping("/collegues")
    public ResponseEntity<List<User>> getColleaguesInSameDepartmentAndService() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = authenticationService.getUserByUsername(username);

        if (currentUser.getDepartement() == null || currentUser.getService() == null) {
            return ResponseEntity.badRequest().build();
        }

        Integer departementId = Math.toIntExact(currentUser.getDepartement().getId());
        Integer serviceId = Math.toIntExact(currentUser.getService().getId());

        return this.getColleaguesByDepartmentAndService(departementId, serviceId);
    }
}