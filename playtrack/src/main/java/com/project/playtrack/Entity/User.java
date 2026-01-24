package com.project.playtrack.Entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.playtrack.Enum.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"User\"")
public class User implements UserDetails {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String userName;
    private String password;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private boolean isPlayer;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Player player;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // e.g., "ADMIN", "CAPTAIN", "PLAYER"

    public User() {}
    
    public User(Long userId, String userName, String password, String email, boolean isPlayer, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.isPlayer = isPlayer;
        this.role = role;
    }

   @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername () { return userName; }

    @Override
    public String getPassword () { return password; }

    @Override
    public boolean isAccountNonExpired () { return true; }

    @Override
    public boolean isAccountNonLocked () { return true; }

    @Override
    public boolean isCredentialsNonExpired () { return true; }

    @Override
    public boolean isEnabled () { return true; }

    // GETTERS AND SETTERS

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
