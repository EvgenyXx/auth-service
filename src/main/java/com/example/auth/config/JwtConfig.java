package com.example.auth.config;

import com.example.auth.util.KeyParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.interfaces.RSAPublicKey;




@Configuration
public class JwtConfig {

    @Bean
    public KeyParser keyParser() {
        return new KeyParser();
    }

    @Bean
    public RSAPublicKey rsaPublicKey(JwtKeyProvider keyProvider) {
        return keyProvider.getRsaPublicKey();
    }
}