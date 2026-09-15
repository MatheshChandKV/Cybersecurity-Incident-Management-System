package com.cims;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String name;
    private Role role;

    public User(int id, String username, String passwordHash,
                String name, Role role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.name = name;
        this.role = role;
    }
    public User(String username, String passwordHash,
                String name, Role role) {
        this(-1, username, passwordHash, name, role);
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getName() { return name; }
    public Role getRole() { return role; }

    public void setId(int id) { this.id = id; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
