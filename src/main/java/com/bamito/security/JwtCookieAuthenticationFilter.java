package com.bamito.security;

import com.bamito.service.implement.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    @Autowired
    private JwtServiceImpl jwtService;
    private final String[] PUBLIC_ENDPOINTS = {
            "/user/register",
            "/user/login",
            "/user/verify-email",
            "/user/regenerate-otp",
            "/user/send-otp",
            "/user/forgot-password",
            "/product/get-all-by-category",
            "/product/get-all-sale",
            "/size/get-all-by-category",
    };

    public JwtCookieAuthenticationFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!isPublicEndpoint(request)) {
            String token = extractTokenFromCookie(request, "accessToken");
            if (token != null && !isTokenExpired(token)) {
                Jwt jwt = jwtDecoder.decode(token);

                Collection<GrantedAuthority> authorities = jwt.getClaimAsStringList("scope")
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList());
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else if (token != null) {
                // Token hết hạn, làm mới token
                String refreshToken = extractTokenFromCookie(request, "refreshToken");

                if (refreshToken != null) {
                    Map<String, String> tokens = jwtService.refreshToken(refreshToken, jwtDecoder);

                    if (!CollectionUtils.isEmpty(tokens)) {
                        // Đặt lại cookie accessToken và refreshToken
                        Cookie accessCookie = new Cookie("accessToken", tokens.get("access"));
                        accessCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
                        accessCookie.setHttpOnly(true);
                        accessCookie.setSecure(true);
                        accessCookie.setPath("/");

                        Cookie refreshCookie = new Cookie("refreshToken", tokens.get("refresh"));
                        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
                        refreshCookie.setHttpOnly(true);
                        refreshCookie.setSecure(true);
                        refreshCookie.setPath("/");

                        response.addCookie(accessCookie);
                        response.addCookie(refreshCookie);

                        Jwt newJwt = jwtDecoder.decode(tokens.get("access"));
                        Collection<GrantedAuthority> authorities = newJwt.getClaimAsStringList("scope")
                                .stream()
                                .map(role -> new SimpleGrantedAuthority(role))
                                .collect(Collectors.toList());

                        JwtAuthenticationToken newAuthentication = new JwtAuthenticationToken(newJwt, authorities);
                        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
                    } else {
                        SecurityContextHolder.clearContext();
                    }
                } else {
                    SecurityContextHolder.clearContext();
                }

            } else {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if (cookie.getName().equals(cookieName)) {
//                    return cookie.getValue();
//                }
//            }
//        }
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        return cookie != null ? cookie.getValue() : null;
    }

    private boolean isTokenExpired(String token) {
        try {
            // Kiểm tra expiration trong access token
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getExpiresAt().isBefore(Instant.now());
        } catch (JwtException e) {
            return true; // Nếu có lỗi khi decode thì coi như token đã hết hạn
        }
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        for (String endpoint : PUBLIC_ENDPOINTS) {
            if (request.getRequestURI().contains(endpoint) || isGetMethodPublicEndpoint(request)) {
                return true;
            }
        }
        return false;
    }

    private boolean isGetMethodPublicEndpoint(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.GET.name()) &&
                (request.getRequestURI().contains("/brand") ||
                        request.getRequestURI().contains("/category") ||
                        request.getRequestURI().contains("/product") ||
                        request.getRequestURI().contains("/product/{id}") ||
                        request.getRequestURI().contains("/size") ||
                        request.getRequestURI().contains("/product-size") ||
                        request.getRequestURI().contains("/feedback"));
    }

}
