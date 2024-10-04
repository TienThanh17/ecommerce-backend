package com.bamito.service.implement;

import com.bamito.entity.Role;
import com.bamito.entity.User;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.service.IJwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtServiceImpl implements IJwtService {
    @NonFinal
    @Value("${jwt.access-duration}")
    protected int ACCESS_DURATION;
    @NonFinal
    @Value("${jwt.refresh-duration}")
    protected int REFRESH_DURATION;
    @NonFinal
    @Value("${jwt.access-key}")
    protected String ACCESS_SIGNER_KEY;
    @NonFinal
    @Value("${jwt.refresh-key}")
    protected String REFRESH_SIGNER_KEY;

    @Override
    public String generateToken(User user, boolean isAccessToken) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("bamito-web-shop")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(isAccessToken ? ACCESS_DURATION : REFRESH_DURATION,
                                ChronoUnit.SECONDS).toEpochMilli()
                ))
//                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("userId", user.getId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(isAccessToken ? ACCESS_SIGNER_KEY.getBytes() : REFRESH_SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new CustomizedException(ErrorCode.CAN_NOT_CREATE_TOKEN);
        }
    }

    private String buildScope(User user) {
        Role role = user.getRole();
        return "ROLE_" + role.getName();
    }
}
