package com.gitpolio.gitpolioserver.jwt;

import com.gitpolio.gitpolioserver.dto.LoginInfoDto;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthTokenUtils {
    private static final String secretKey = "test";

    public static String generateJwtToken(LoginInfoDto loginInfo) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(loginInfo.getId())
                .setHeader(createHeader())
                .setClaims(createClaims(loginInfo))
                .setExpiration(createExpireDate(30))
                .signWith(SignatureAlgorithm.HS256, createSigningKey());

        return builder.compact();
    }

    public static boolean isValidToken(String token) {
        try {
            getClaimsFormToken(token);
            return true;
        } catch (NullPointerException e) {
            throw new JwtException("token is null");
        }
    }

    public static String getAccountIdFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return (String) claims.get("id");
    }

    private static Date createExpireDate(int date) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, date);
        return c.getTime();
    }

    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    private static Map<String, Object> createClaims(LoginInfoDto loginInfo) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", loginInfo.getId());

        return claims;
    }

    private static Key createSigningKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private static Claims getClaimsFormToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token).getBody();
    }
}
