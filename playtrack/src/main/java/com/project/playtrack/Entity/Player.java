package com.project.playtrack.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*=============================================================================
firstname      → @Column(name = "firstname")
lastname       → @Column(name = "lastname")
username       → @Column(name = "username")
email          → @Column(name = "email")
position       → @Column(name = "position")
role           → @Column(name = "role")
jerseynumber   → @Column(name = "jerseynumber")
=============================================================================*/

@Entity
@Table (name = "player")
public class Player {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY) // auto increment id
    @Column(name = "playerid")
    private Long playerid;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "position")
    private String position;

    @Column(name = "role")
    private String role;

    @Column(name = "jerseynumber")
    private Integer jerseyNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "team")   // column = team
    private Team team;

    public Player (String email, String firstName, Integer jerseyNumber, String lastName, String position, String role, String userName) {
        this.email = email;
        this.firstName = firstName;
        this.jerseyNumber = jerseyNumber;
        this.lastName = lastName;
        this.position = position;
        this.role = role;
        this.username = userName;
    }

    // REQUIRED empty constructor
    public Player() {}

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Attendance> attendances = new ArrayList<>();

    public List<Attendance> getAttendances() {
        return attendances;
    }

    // GETTERS AND SETTERS
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public Long getId() {
        return playerid;
    }

    public String getFullName () {
        return this.firstName + " " + this.lastName;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}