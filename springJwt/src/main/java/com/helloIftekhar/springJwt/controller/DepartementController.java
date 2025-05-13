package com.helloIftekhar.springJwt.controller;

import com.helloIftekhar.springJwt.dto.DepartementDTO;
import com.helloIftekhar.springJwt.service.DepartementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departements")
public class DepartementController {

    private final DepartementService departementService;

    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartementDTO> createDepartement(@RequestBody DepartementDTO departementDTO) {
        DepartementDTO createdDepartement = departementService.createDepartement(departementDTO);
        return ResponseEntity.ok(createdDepartement);
    }

    @GetMapping
    public ResponseEntity<List<DepartementDTO>> getAllDepartements() {
        List<DepartementDTO> departements = departementService.getAllDepartements();
        return ResponseEntity.ok(departements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartementDTO> getDepartementById(@PathVariable Long id) {
        DepartementDTO departement = departementService.getDepartementById(id);
        return ResponseEntity.ok(departement);
    }

    @GetMapping("/details")
    public ResponseEntity<List<DepartementDTO>> getAllDepartementsWithDetails() {
        List<DepartementDTO> departements = departementService.getAllDepartementsWithDetails();
        return ResponseEntity.ok(departements);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartementDTO> updateDepartement(
            @PathVariable Long id,
            @Valid @RequestBody DepartementDTO departementDTO) {

        DepartementDTO updatedDepartement = departementService.updateDepartement(id, departementDTO);
        return ResponseEntity.ok(updatedDepartement);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Long id) {
        departementService.deleteDepartement(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/nombre-employes")
    public ResponseEntity<Integer> getNombreEmployesParDepartement(@PathVariable Long id) {
        int nombre = departementService.getNombreEmployesByDepartementId(id);
        return ResponseEntity.ok(nombre);
    }

}