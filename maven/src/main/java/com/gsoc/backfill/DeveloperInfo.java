package com.gsoc.backfill;

public class DeveloperInfo {
    private String ldap;
    private String github;

    public DeveloperInfo(String ldap, String github) {
        this.ldap = ldap;
        this.github = github;
    }

    public String getLdapUsername() {
        return ldap;
    }

    public void setLdapUsername(String ldap) {
        this.ldap = ldap;
    }

    public String getGithubUsername() {
        return github;
    }

    public void setGithubUsername(String github) {
        this.github = github;
    }
}

