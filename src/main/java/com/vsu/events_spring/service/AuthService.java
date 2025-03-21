package com.vsu.events_spring.service;

import com.vsu.events_spring.dto.request.JwtRequest;
import com.vsu.events_spring.dto.response.JwtResponse;
import com.vsu.events_spring.dto.response.ProfileDTO;
import com.vsu.events_spring.entity.JwtAuthentication;
import com.vsu.events_spring.entity.Profile;
import com.vsu.events_spring.repository.ProfileRepository;
import com.vsu.events_spring.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final Profile profile = profileRepository.findByUserName(authRequest.getUsername());
        if (profile == null) {
            throw new AuthException("Username not found");
        }

        if (passwordEncoder.matches(authRequest.getPassword(), profile.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(profile);
            final String refreshToken = jwtProvider.generateRefreshToken(profile);
            final Long profileId = profile.getId_profile();
            final Integer count = refreshTokenRepository.checkRefreshToken(profile.getId_profile());
            if (null != count && count > 0){
                refreshTokenRepository.updateRefreshToken(profile.getId_profile(), refreshToken);
            }
            else {
                refreshTokenRepository.saveToken(refreshToken, profile.getId_profile());
            }

            return new JwtResponse(accessToken, refreshToken, profileId);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final Long profileId = Long.parseLong(claims.getId());
            final String saveRefreshToken = refreshTokenRepository.findByProfileId(profileId);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final Profile profile = profileRepository.findById(profileId);
                final String accessToken = jwtProvider.generateAccessToken(profile);
                return new JwtResponse(accessToken, null, profileId);
            }
        }
        return new JwtResponse(null, null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final Long profileId = Long.parseLong(claims.getId());
            final String saveRefreshToken = refreshTokenRepository.findByProfileId(profileId);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final Profile profile = profileRepository.findById(profileId);
                final String accessToken = jwtProvider.generateAccessToken(profile);
                final String newRefreshToken = jwtProvider.generateRefreshToken(profile);
                refreshTokenRepository.updateRefreshToken(profile.getId_profile(), newRefreshToken);
               // refreshStorage.put(profile.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken, profileId);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
