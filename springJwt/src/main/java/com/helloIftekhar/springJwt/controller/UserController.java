package com.helloIftekhar.springJwt.controller;

import com.helloIftekhar.springJwt.dto.DtoMapper;
import com.helloIftekhar.springJwt.dto.UserDTO;
import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthenticationService authenticationService;

    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authenticationService.getUserByUsername(username);
        return ResponseEntity.ok(DtoMapper.convertToUserDTO(user));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        User user = authenticationService.getUserByUsername(username);
        return ResponseEntity.ok(DtoMapper.convertToUserDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = authenticationService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(DtoMapper::convertToUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody User updatedUser) {
        // Récupérer l'utilisateur actuellement authentifié
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = authenticationService.getUserByUsername(username);

        // Mettre à jour les informations de l'utilisateur
        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setMatricule(updatedUser.getMatricule());
        currentUser.setGrade(updatedUser.getGrade());
        currentUser.setDateNaissance(updatedUser.getDateNaissance());
        currentUser.setAdresse(updatedUser.getAdresse());
        currentUser.setEchelle(updatedUser.getEchelle());

        // Si le département est fourni dans la mise à jour
        if (updatedUser.getDepartement() != null && updatedUser.getDepartement().getId() != null) {
            currentUser.setDepartement(updatedUser.getDepartement());
        }

        // Sauvegarder les modifications
        User savedUser = authenticationService.updateUser(currentUser);

        return ResponseEntity.ok(DtoMapper.convertToUserDTO(savedUser));
    }
}