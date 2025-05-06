package com.helloIftekhar.springJwt.controller;

import com.helloIftekhar.springJwt.dto.SoldeCongeDTO;
import com.helloIftekhar.springJwt.model.SoldeConge;
import com.helloIftekhar.springJwt.service.SoldeCongeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/solde-conge")
public class SoldeCongeController {

    private final SoldeCongeService soldeCongeService;

    public SoldeCongeController(SoldeCongeService soldeCongeService) {
        this.soldeCongeService = soldeCongeService;
    }

    @GetMapping("/mon-solde")
    public ResponseEntity<SoldeCongeDTO> getMonSolde() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        SoldeCongeDTO solde = soldeCongeService.getSoldeByCurrentUser(username);

        // Convertir en DTO
        SoldeCongeDTO response = new SoldeCongeDTO(
                solde.getId(),
                solde.getSoldeGlobal(),
                solde.getSoldeAnneeCourante(),
                solde.getSoldeAnneePrecedente(),
                solde.getAnnee()
        );

        return ResponseEntity.ok(response);
    }
}