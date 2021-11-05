package com.codercampus.api.repository;

import com.codercampus.api.model.RefreshToken;
import com.codercampus.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

    Long deleteByUser(User user);
}
