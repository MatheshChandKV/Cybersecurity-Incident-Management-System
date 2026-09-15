package com.cims;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public User login(String username, String password) throws SQLException {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty.");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be empty.");

        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        String hashed = hashPassword(password);
        if (!hashed.equalsIgnoreCase(user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        return user;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available.", e);
        }
    }
}
