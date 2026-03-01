package com.project.playtrack.User;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
    private Long userId;
    private String email;
    private Set<String> roles;
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Set<String> getRoles() { return roles; }
    public void setRoles (Set<String> roles) { this.roles = roles; }
}