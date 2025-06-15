package com.example.auth.config;


import com.example.auth.util.KeyParser;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
@Getter
public class JwtKeyProvider {
    private final PrivateKey privateKey;
    private final RSAPublicKey rsaPublicKey;
    private final KeyParser keyParser;

    public JwtKeyProvider(
            @Value("${spring.security.jwt.private-key-location}") Resource privateKeyResource,
            @Value("${spring.security.jwt.public-key-location}") Resource publicKeyResource,
            KeyParser keyParser
    ) throws Exception {
        this.keyParser = keyParser;
        this.privateKey = keyParser.parsePrivateKey(loadKeyFromResource(privateKeyResource));
        this.rsaPublicKey = keyParser.parseRSAPublicKey(loadKeyFromResource(publicKeyResource));
    }

    private String loadKeyFromResource(Resource resource) throws Exception {
        return new String(resource.getInputStream().readAllBytes());
    }
}