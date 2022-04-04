package com.codercampus.api.service;

import com.codercampus.api.exception.RefreshTokenException;
import com.codercampus.api.model.RefreshToken;
import com.codercampus.api.repository.RefreshTokenRepository;
import com.codercampus.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
public class RefreshTokenService {

    @Value("${codercampus.app.refreshTokenExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository
    ){
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    // find token in the database
    public Optional<RefreshToken> findByToken(String token) {
        return this.refreshTokenRepository.findByToken(token);
    }


    // create new refresh token
    public RefreshToken createRefreshToken(Long userId) {

        // init new refresh token
        RefreshToken refreshToken = new RefreshToken();

        // set data on refresh token
        refreshToken.setUser(this.userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        // persist the newly created refresh token in the database
        refreshToken = this.refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    // verifies refresh token expiration, if it is expired then deletes the token from the database
    @Modifying
    public RefreshToken verifyExpiration(RefreshToken token) {

        // check if token has benn expired
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            this.refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        // otherwise return the token
        return token;
    }

    // deletes the token based on a given user ID
    @Transactional
    public Long deleteByUserId(Long userId) {
        return this.refreshTokenRepository.deleteByUser(this.userRepository.findById(userId).get());
    }
}
