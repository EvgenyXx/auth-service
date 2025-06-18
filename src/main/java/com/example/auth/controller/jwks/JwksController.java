package com.example.auth.controller.jwks;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
public class JwksController {
    private final RSAPublicKey publicKey;

    public JwksController(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getJwks() {
        return Map.of(
                "keys", List.of(
                        Map.of(
                                "kty", "RSA",
                                "alg", "RS256",
                                "n", Base64.getUrlEncoder().encodeToString(publicKey.getModulus().toByteArray()),
                                "e", Base64.getUrlEncoder().encodeToString(publicKey.getPublicExponent().toByteArray())
                        )
                )
        );
    }
}

