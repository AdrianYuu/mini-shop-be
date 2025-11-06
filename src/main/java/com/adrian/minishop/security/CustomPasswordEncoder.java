package com.adrian.minishop.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CustomPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(12);

    @Override
    public String encode(CharSequence rawPassword) {
        String sha256Base64 = sha256Base64(rawPassword.toString());
        return bcrypt.encode(sha256Base64);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String sha256Base64 = sha256Base64(rawPassword.toString());
        return bcrypt.matches(sha256Base64, encodedPassword);
    }

    private String sha256Base64(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA256 not supported");
        }
    }

}
