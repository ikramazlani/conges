package com.helloIftekhar.springJwt.service;

import com.helloIftekhar.springJwt.repository.TokenRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class TokenCleanupService {
    private final TokenRepository tokenRepository;

    public TokenCleanupService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @PreDestroy
    public void clearTokensOnShutdown() {
        tokenRepository.deleteAll(); // Vide la table token
        System.out.println("Tous les tokens ont été supprimés lors de l'arrêt de l'application !");
    }
}