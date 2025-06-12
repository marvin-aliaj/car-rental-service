package com.spring.jdbc.car.rental.service;

import com.spring.jdbc.car.rental.exception.ForbiddenException;
import com.spring.jdbc.car.rental.utils.AESUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.spring.jdbc.car.rental.model.enums.CONSTANT_VARIABLES.TOKEN_VALIDITY_IN_MILLISECONDS;

@Service
public class AuthenticationService {

    @Value("${car.rental.service.secret.key}")
    private String secretKey;
    @Value("${car.rental.service.admin.username}")
    private String username;
    @Value("${car.rental.service.admin.password}")
    private String password;

    public boolean hasAccess(String key) {
        return Objects.equals(secretKey, key);
    }

    public boolean validateToken(String token) throws Exception {
        if (token == null || token.isEmpty()) {
            return false;
        }
        // Split the data to retrieve the username, password, and timestamp
        Map<String, String> foundUser = getCurrentUser(token);
        if (foundUser == null) {
            return false;
        }
        if (validateUser(foundUser) != null) {
            String[] parts = token.split(":");
            long timestamp = Long.parseLong(AESUtil.decrypt(parts[2]));

            // Check if the token is still valid based on the timestamp
            long currentTime = new Date().getTime();
            return (currentTime - timestamp) <= TOKEN_VALIDITY_IN_MILLISECONDS;
        } else {
            return false;
        }
    }

    public Map<String, String> getCurrentUser(String token) throws Exception {
        String[] parts = token.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token format");
        }

        String username = AESUtil.decrypt(parts[0]);
        String password = AESUtil.decrypt(parts[1]);
        return Map.of("username", username, "password", password);
    }

    public String validateUser(Map<String, String> user) throws Exception {
        try {
            if (Objects.equals(username, user.get("username")) && Objects.equals(password, user.get("password"))) {
                return createToken(user.get("username"), user.get("password"));
            } else {
                throw new ForbiddenException("Invalid username or password");
            }
        } catch (ForbiddenException e) {
            throw new ForbiddenException();
        }
    }

    // Method to create a token
    public String createToken(String username, String password) throws Exception {
        // Combine the username, password, and timestamp into a single string
        String timestamp = String.valueOf(new Date().getTime());

        String usernameEncrypt = AESUtil.encrypt(username);
        String passwordEncrypt = AESUtil.encrypt(password);
        String timestampEncrypt = AESUtil.encrypt(timestamp);
        // Encrypt the data
        return usernameEncrypt + ":" + passwordEncrypt + ":" + timestampEncrypt;
    }
}
