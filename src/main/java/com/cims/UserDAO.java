package com.cims;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password, name, role FROM users WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, name, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getName());
            ps.setString(4, user.getRole().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, username, password, name, role FROM users";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<User> getAnalysts() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, username, password, name, role FROM users WHERE role = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, Role.ANALYST.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String passwordHash = rs.getString("password");
        String name = rs.getString("name");
        Role role = Role.valueOf(rs.getString("role"));
        return new User(id, username, passwordHash, name, role);
    }
}
