package com.example.springOauth2.service;

import java.io.File;
import java.io.FileReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.function.Function;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    private final String PRIVATE_KEY_PATH = "keys/private.pem";
    private final String PUBLIC_KEY_PATH = "keys/public.pem";

    Logger logger = LoggerFactory.getLogger(getClass());

    public String extractUsername(String token) {
        // logger.info("extractUsername ::: ");
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // logger.info("extractClaim ::: ");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // logger.info("extractAllClaims ::: ");

        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSignInKey() {
        // logger.info("getSignInKey ::: ");

        ClassPathResource publicKeyResource = new ClassPathResource(PUBLIC_KEY_PATH);
        File publicKeyFile;
        try {
        publicKeyFile = publicKeyResource.getFile();
        return readPublicKey(publicKeyFile);
        } catch (Exception e) {
        e.printStackTrace();
        }
        return null;
    }

    public String generateToken(UserDetails userDetails) throws Exception {
        return buildToken(userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) throws Exception {
        return buildToken(userDetails, refreshExpiration);
    }

    private String buildToken(UserDetails userDetails, long expiration) throws Exception {
        Claims claims = Jwts.claims();
        claims.put("accessibilities", userDetails.getAuthorities());

        ClassPathResource privateKeyResource = new ClassPathResource(PRIVATE_KEY_PATH);
        File privateKeyFile = privateKeyResource.getFile();
        PrivateKey readPrivateKey = readPrivateKey(privateKeyFile);
        JwtBuilder jwtsBuilder = Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setIssuer("http://localhost:8080/api")
                .signWith(readPrivateKey, SignatureAlgorithm.RS256);
        return jwtsBuilder.compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // logger.info("isTokenValid ::: " + username);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private PublicKey readPublicKey(File file) throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA");

        try (FileReader keyReader = new FileReader(file);
        PemReader pemReader = new PemReader(keyReader)) {

            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
            return (PublicKey) factory.generatePublic(pubKeySpec);
        }
    }

    private PrivateKey readPrivateKey(File file) throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        try (FileReader keyReader = new FileReader(file);
        PemReader pemReader = new PemReader(keyReader)) {

        PemObject pemObject = pemReader.readPemObject();
        byte[] content = pemObject.getContent();
        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
        return (PrivateKey) factory.generatePrivate(privKeySpec);
        }
    }
}