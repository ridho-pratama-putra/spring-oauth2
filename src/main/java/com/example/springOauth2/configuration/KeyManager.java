package com.example.springOauth2.configuration;

import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.jwk.RSAKey;

@Component
public class KeyManager {

    private final String PRIVATE_KEY_PATH = "keys/private.pem";
    private final String PUBLIC_KEY_PATH = "keys/public.pem";

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

    public RSAKey rsaKey() {
        ClassPathResource privateKeyResource = new ClassPathResource(PRIVATE_KEY_PATH);
        ClassPathResource publicKeyResource = new ClassPathResource(PUBLIC_KEY_PATH);
        try {
            File publicKeyFile = publicKeyResource.getFile();
            File privateKeyFile = privateKeyResource.getFile();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) readPublicKey(publicKeyFile);
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) readPrivateKey(privateKeyFile);

            return new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).keyID(UUID.randomUUID().toString()).build();

        } catch (Exception e) {
            throw new RuntimeException("check public & private key");
        }
    }
}
