package com.gsoc.backfill;

public class AdditionalTeamDefinition {
    private String teamName;
    private Role role;

    public AdditionalTeamDefinition(String teamName, String role) {
        this.teamName = teamName;
        this.role = validateRole(role);
    }

    public String getName() {
        return teamName;
    }

    public void setName(String teamName) {
        this.teamName = teamName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    private Role validateRole(String role) {
        if (role == null) {
            return null;
        }
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid team role: " + role);
        }
    }
}

