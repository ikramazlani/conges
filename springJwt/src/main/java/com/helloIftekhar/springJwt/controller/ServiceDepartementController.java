package com.helloIftekhar.springJwt.controller;

import com.helloIftekhar.springJwt.dto.ServiceDepartementDTO;
import com.helloIftekhar.springJwt.dto.UserDTO;
import com.helloIftekhar.springJwt.service.ServiceDepartementService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services-departement")
public class ServiceDepartementController {

    private final ServiceDepartementService service;

    public ServiceDepartementController(ServiceDepartementService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDepartementDTO> createService(@RequestBody ServiceDepartementDTO dto) {
        ServiceDepartementDTO createdService = service.createService(dto);
        return ResponseEntity.ok(createdService);
    }

    @GetMapping("/departement/{departementId}")
    public ResponseEntity<List<ServiceDepartementDTO>> getServicesByDepartement(
            @PathVariable Long departementId) {
        List<ServiceDepartementDTO> services = service.getServicesByDepartement(departementId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/departement/{departementId}/all")
    public ResponseEntity<List<ServiceDepartementDTO>> getAllServicesByDepartement(
            @PathVariable Long departementId) {
        List<ServiceDepartementDTO> services = service.getAllServicesByDepartement(departementId);
        return ResponseEntity.ok(services);
    }


    @PutMapping("/{serviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateService(
            @PathVariable Long serviceId,
            @RequestBody ServiceDepartementDTO dto) {
        try {
            ServiceDepartementDTO updatedService = service.updateService(serviceId, dto);
            return ResponseEntity.ok(updatedService);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteService(@PathVariable Long serviceId) {
        service.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceDepartementDTO>> getAllServices() {
        List<ServiceDepartementDTO> services = service.getAllServices();
        return ResponseEntity.ok(services);
    }
//developer par chef service
    @GetMapping("/departement/{departementId}/service/{serviceId}/users")
    public ResponseEntity<List<UserDTO>> getUsersByServiceAndDepartement(
            @PathVariable Long departementId,
            @PathVariable Long serviceId) {
        List<UserDTO> users = service.getUsersByServiceAndDepartement(serviceId, departementId);
        return ResponseEntity.ok(users);
    }
}