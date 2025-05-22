package com.helloIftekhar.springJwt.service;


import com.helloIftekhar.springJwt.model.*;
import com.helloIftekhar.springJwt.repository.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
@Service
public class AuthenticationService {

    @Autowired
    private DemandeCongeRepository demandeCongeRepository;

    @Autowired
   private EmailService emailService;

    private final Map<String, String> resetCodes = new HashMap<>();
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final DepartementRepository departementRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final SoldeCongeRepository soldeCongeRepository;


    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final ServiceDepartementRepository serviceDepartementRepository;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService, DepartementRepository departementRepository,
                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager,SoldeCongeRepository soldeCongeRepository,ServiceDepartementRepository serviceDepartementRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.departementRepository = departementRepository;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.soldeCongeRepository = soldeCongeRepository;
        this.serviceDepartementRepository=serviceDepartementRepository;

    }



    public AuthenticationResponse register(User request) {
        if(repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, null,"User already exist",null);
        }

        // Check if departement exists
        if(request.getDepartement() == null || request.getDepartement().getId() == null) {
            return new AuthenticationResponse(null, null,"Departement is required",null);
        }

        ServiceDepartement serviceDepartement = null;
        if (request.getService() != null && request.getService().getId() != null) {
            serviceDepartement = serviceDepartementRepository.findById(request.getService().getId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
        }


        Departement departement = departementRepository.findById(request.getDepartement().getId())
                .orElseThrow(() -> new RuntimeException("Departement not found"));


        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDepartement(departement); // Use the fetched departement
        user.setRole(request.getRole());
        user.setMatricule(request.getMatricule());
        user.setGrade(request.getGrade());
        user.setDateNaissance(request.getDateNaissance());
        user.setAdresse(request.getAdresse());
        user.setEchelle(request.getEchelle());
        user.setEmail(request.getEmail());
        user.setService(serviceDepartement);
        //ajouter des hcmaps pour arab
        user.setNomArab(request.getNomArab());
        user.setPrenomArab(request.getPrenomArab());
        user.setGradeArab(request.getGradeArab());
        user.setEchelleArab(request.getEchelleArab());

        user = repository.save(user);
        createInitialSoldeConge(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken,"User registration was successful",user.getRole());
    }


    private void createInitialSoldeConge(User user) {
        SoldeConge soldeConge = new SoldeConge();
        soldeConge.setEmployee(user);
        soldeConge.setSoldeGlobal(22); // Valeur par défaut
        soldeConge.setSoldeAnneeCourante(22); // Valeur par défaut
        soldeConge.setSoldeAnneePrecedente(0); // Valeur par défaut
        soldeConge.setAnnee(Year.now().getValue()); // Année courante

        soldeCongeRepository.save(soldeConge);
    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken, "User login was successful", user.getRole());

    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        // extract the token from authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // extract username from token
        String username = jwtService.extractUsername(token);

        // check if the user exist in database
        User user = repository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("No user found"));

        // check if the token is valid
        if(jwtService.isValidRefreshToken(token, user)) {
            // generate access token
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity(new AuthenticationResponse(accessToken, refreshToken, "New token generated",user.getRole()), HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.UNAUTHORIZED);

    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public List<User> getAllUsers() {
        return repository.findAll(); // Cette méthode est fournie par JpaRepository
    }

    public User updateUser(User user) {
        return repository.save(user);
    }



    public List<User> getUsersByDepartement(Integer departementId) {
        return repository.findByDepartementId(departementId);
    }


    public User updateUserInfo(User existingUser, User updatedUser) {
        // Update basic information
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setMatricule(updatedUser.getMatricule());
        existingUser.setGrade(updatedUser.getGrade());
        existingUser.setDateNaissance(updatedUser.getDateNaissance());
        existingUser.setAdresse(updatedUser.getAdresse());
        existingUser.setEchelle(updatedUser.getEchelle());

        //ajouter des champs arab
        existingUser.setNomArab(updatedUser.getNomArab());
        existingUser.setPrenomArab(updatedUser.getPrenomArab());
        existingUser.setGradeArab(updatedUser.getGradeArab());
        existingUser.setEchelleArab(updatedUser.getEchelleArab());

        // Update role if provided
        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

        // Update department if provided
        if (updatedUser.getDepartement() != null && updatedUser.getDepartement().getId() != null) {
            Departement departement = departementRepository.findById(updatedUser.getDepartement().getId())
                    .orElseThrow(() -> new RuntimeException("Departement not found"));
            existingUser.setDepartement(departement);
        }

        // Update service if provided
        if (updatedUser.getService() != null && updatedUser.getService().getId() != null) {
            ServiceDepartement service = serviceDepartementRepository.findById(updatedUser.getService().getId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            existingUser.setService(service);
        }

        return repository.save(existingUser);
    }




    @Transactional
    public void deleteUserWithDependencies(String username) {
        // 1. Trouver l'utilisateur
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // 2. Déconnecter l'utilisateur s'il est connecté
        revokeAllTokenByUser(user);

        // 3. Supprimer toutes les demandes de congé de l'utilisateur
        demandeCongeRepository.deleteByIdEmployee(Long.valueOf(user.getId()));

        // 4. Supprimer les soldes de congé de l'utilisateur
        soldeCongeRepository.deleteByEmployeeId(Long.valueOf(user.getId()));

        // 5. Finalement supprimer l'utilisateur
        repository.delete(user);
    }



    public long countAllUsers() {
        return repository.count();
    }

    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Nombre total d'utilisateurs
        stats.put("totalUsers", repository.count());

        // Nombre d'utilisateurs par rôle
        Map<String, Long> usersByRole = repository.findAll().stream()
                .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));
        stats.put("usersByRole", usersByRole);

        // Nombre d'utilisateurs par département
        Map<String, Long> usersByDepartement = repository.findAll().stream()
                .filter(user -> user.getDepartement() != null)
                .collect(Collectors.groupingBy(
                        user -> user.getDepartement().getNomDepartement(),
                        Collectors.counting()));
        stats.put("usersByDepartement", usersByDepartement);

        return stats;
    }


    public List<User> getUsersByDepartementAndService(Integer departementId, Integer serviceId) {
        return repository.findByDepartementIdAndServiceId(departementId, serviceId);
    }


    //change de mot de passe

    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Vérification du mot de passe actuel
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Validation du nouveau mot de passe
        if (newPassword == null || newPassword.length() < 8) {
            throw new RuntimeException("New password must be at least 8 characters");
        }

        // Mise à jour du mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);

        // Révoquer tous les tokens
        revokeAllTokenByUser(user);
    }





    // Ajoutez ces méthodes à AuthenticationService pour rein

    public ResponseEntity<?> initiatePasswordReset(String email) {
        try {
            // 1. Vérifier l'email
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body("L'email est requis");
            }

            // 2. Rechercher l'utilisateur
            List<User> users = repository.findByEmail(email);

            if (users.isEmpty()) {
                return ResponseEntity.badRequest().body("Aucun utilisateur trouvé avec cet email");
            }

            if (users.size() > 1) {
                logger.error("Conflit d'email : Plusieurs utilisateurs trouvés pour {}", email);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Plusieurs comptes associés à cet email. Contactez l'administrateur.");
            }

            User user = users.get(0);

            // 3. Générer un code de réinitialisation
            String resetCode = emailService.generateResetCode();

            // 4. Stocker le code (avec expiration)
            resetCodes.put(email, resetCode);

            // 5. Envoyer l'email
            emailService.sendResetCode(email, resetCode);

            // 6. Journaliser l'action
            logger.info("Demande de réinitialisation pour {} - Code envoyé", email);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Un code de réinitialisation a été envoyé à votre email",
                            "status", "SUCCESS"
                    )
            );

        } catch (MailException e) {
            logger.error("Échec d'envoi d'email à {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'envoi du code. Veuillez réessayer plus tard.");
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la réinitialisation pour {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur inattendue s'est produite");
        }
    }

    public ResponseEntity<?> resetPassword(String email, String resetCode, String newPassword) {
        // 1. Trouver l'utilisateur
        List<User> users = repository.findByEmail(email);

        if (users.isEmpty()) {
            return ResponseEntity.badRequest().body("Aucun utilisateur trouvé avec cet email");
        }

        if (users.size() > 1) {
            logger.error("Conflit: plusieurs utilisateurs avec l'email {}", email);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Plusieurs comptes associés à cet email");
        }

        User user = users.get(0); // Prendre le premier utilisateur

        // 2. Vérifier le code de réinitialisation
        String storedCode = resetCodes.get(email);
        if (storedCode == null || !storedCode.equals(resetCode)) {
            return ResponseEntity.badRequest().body("Code de réinitialisation invalide");
        }

        // 3. Mettre à jour le mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);

        // 4. Supprimer le code utilisé
        resetCodes.remove(email);

        return ResponseEntity.ok("Mot de passe réinitialisé avec succès");
    }


}


