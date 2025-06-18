package com.movieAPIPractice.SKMoviebhandar.authorization.repositories;

import com.movieAPIPractice.SKMoviebhandar.authorization.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
