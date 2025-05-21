package com.helloIftekhar.springJwt.controller;

import com.helloIftekhar.springJwt.dto.DtoMapper;
import com.helloIftekhar.springJwt.dto.UserDTO;
import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.repository.UserRepository;
import com.helloIftekhar.springJwt.service.AuthenticationService;
import com.helloIftekhar.springJwt.service.ServiceDepartementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public UserController(AuthenticationService authenticationService, UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
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
        currentUser.setService(updatedUser.getService());

        // Si le département est fourni dans la mise à jour
        if (updatedUser.getDepartement() != null && updatedUser.getDepartement().getId() != null) {
            currentUser.setDepartement(updatedUser.getDepartement());
        }

        // Sauvegarder les modifications
        User savedUser = authenticationService.updateUser(currentUser);

        return ResponseEntity.ok(DtoMapper.convertToUserDTO(savedUser));
    }


    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsersUnfiltered() {
        List<User> users = authenticationService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(DtoMapper::convertToUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/by-departement/{departementId}")
    public ResponseEntity<List<UserDTO>> getUsersByDepartement(@PathVariable Integer departementId) {
        List<User> users = authenticationService.getUsersByDepartement(departementId);
        List<UserDTO> userDTOs = users.stream()
                .map(DtoMapper::convertToUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }


    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User user) {
        // Vérifier que l'username dans le path correspond à l'username dans le body
        if (!username.equals(user.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authenticationService.updateUser(user));
    }


    @PutMapping("/update/{username}")
    public ResponseEntity<UserDTO> updateUserInfo(
            @PathVariable String username,
            @RequestBody User updatedUser) {

        // Check if the user exists
        User existingUser = authenticationService.getUserByUsername(username);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Update the user information
        User savedUser = authenticationService.updateUserInfo(existingUser, updatedUser);

        return ResponseEntity.ok(DtoMapper.convertToUserDTO(savedUser));
    }


    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            authenticationService.deleteUserWithDependencies(username);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAllUsers() {
        long count = authenticationService.countAllUsers();
        return ResponseEntity.ok(count);
    }

    // Endpoint pour obtenir des statistiques sur les utilisateurs
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        Map<String, Object> stats = authenticationService.getUserStatistics();
        return ResponseEntity.ok(stats);
    }

    //hhhhhh
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String, String> request
    ) {
        try {
            authenticationService.changePassword(
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    request.get("currentPassword"),
                    request.get("newPassword")
            );
            return ResponseEntity.ok().body(Map.of("message", "Password changed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }



}

