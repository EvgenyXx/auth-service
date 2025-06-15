package com.example.auth.util;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyParser {
    private static final String KEY_ALGORITHM = "RSA";
    private static final String PRIVATE_KEY_HEADER = "-----BEGIN PRIVATE KEY-----";
    private static final String PRIVATE_KEY_FOOTER = "-----END PRIVATE KEY-----";
    private static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----";
    private static final String PUBLIC_KEY_FOOTER = "-----END PUBLIC KEY-----";

    public PrivateKey parsePrivateKey(String privateKeyStr) throws Exception {
        String privateKeyContent = cleanKey(
                privateKeyStr,
                PRIVATE_KEY_HEADER,
                PRIVATE_KEY_FOOTER
        );

        byte[] decoded = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    public RSAPublicKey parseRSAPublicKey(String publicKeyStr) throws Exception {
        PublicKey publicKey = parsePublicKey(publicKeyStr);
        if (!(publicKey instanceof RSAPublicKey)) {
            throw new IllegalArgumentException("Provided key is not an RSA public key");
        }
        return (RSAPublicKey) publicKey;
    }

    private PublicKey parsePublicKey(String publicKeyStr) throws Exception {
        String publicKeyContent = cleanKey(
                publicKeyStr,
                PUBLIC_KEY_HEADER,
                PUBLIC_KEY_FOOTER
        );

        byte[] decoded = Base64.getDecoder().decode(publicKeyContent);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    private String cleanKey(String keyStr, String header, String footer) {
        return keyStr.replace(header, "")
                .replace(footer, "")
                .replaceAll("\\s", "");
    }
}
