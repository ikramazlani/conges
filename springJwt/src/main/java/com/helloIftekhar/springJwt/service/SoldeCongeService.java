package com.helloIftekhar.springJwt.service;

import com.helloIftekhar.springJwt.dto.SoldeCongeDTO;
import com.helloIftekhar.springJwt.model.SoldeConge;
import com.helloIftekhar.springJwt.model.User;
import com.helloIftekhar.springJwt.repository.SoldeCongeRepository;
import com.helloIftekhar.springJwt.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.Year;
import java.util.Optional;

@Service
public class SoldeCongeService {

    private final SoldeCongeRepository soldeCongeRepository;
    private final UserRepository userRepository;

    public SoldeCongeService(SoldeCongeRepository soldeCongeRepository,
                             UserRepository userRepository) {
        this.soldeCongeRepository = soldeCongeRepository;
        this.userRepository = userRepository;
    }


    public SoldeCongeDTO getSoldeByCurrentUser(String username) {
        // Get only user ID with a lightweight query
        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Integer currentYear = Year.now().getValue();

        // Try to get current year's solde
        Optional<SoldeCongeDTO> solde = soldeCongeRepository
                .findSoldeByUserIdAndYear(userId, currentYear);

        // If not found, get the latest solde
        return solde.orElseGet(() -> soldeCongeRepository
                .findLatestSoldeByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No solde found")));
    }
}