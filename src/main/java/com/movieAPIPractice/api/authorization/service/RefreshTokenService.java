package com.movieAPIPractice.SKMoviebhandar.authorization.service;

import com.movieAPIPractice.SKMoviebhandar.authorization.entities.RefreshToken;
import com.movieAPIPractice.SKMoviebhandar.authorization.entities.User;
import com.movieAPIPractice.SKMoviebhandar.authorization.repositories.RefreshTokenRepository;
import com.movieAPIPractice.SKMoviebhandar.authorization.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserRepository userRepository,RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository=userRepository;
    }

    public RefreshToken createRefreshToken(String userName){
        User user=userRepository.findByUserName(userName).orElseThrow(()->
                new UsernameNotFoundException("User Not found with this name : "+userName));

        RefreshToken refreshToken=user.getRefreshToken();

        if(refreshToken== null){
            refreshToken=RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(24*60*60*10000))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new RuntimeException("Refresh Token Not found"));

        if(token.getExpirationTime().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token is expired ");
        }
        return token;
    }
}
