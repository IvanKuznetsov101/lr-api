package com.vsu.events_spring.service;

import com.vsu.events_spring.entity.JwtAuthentication;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setEmail(claims.getSubject());
        jwtInfoToken.setId(Long.parseLong(claims.getId()));

        return jwtInfoToken;
    }

}
