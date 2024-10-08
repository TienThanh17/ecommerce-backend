package com.bamito.service;

import com.bamito.entity.User;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.Map;

public interface IJwtService {
    String generateToken(User user, boolean isAccessToken);
    Map<String, String> refreshToken(String refreshToken, JwtDecoder jwtDecoder);
}
