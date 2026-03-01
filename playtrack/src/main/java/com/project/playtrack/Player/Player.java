package com.project.playtrack.Player;

import java.util.ArrayList;
import java.util.List;

import com.project.playtrack.Attendance.Attendance;
import com.project.playtrack.Profile.Profile;
import com.project.playtrack.Team.Team;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name="players")
public class Player {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY) // auto increment id
    @Column(name = "player_id")
    private Long id;

    @Column(name = "position", length = 50)
    private String position;

    @Column(name = "jersey_number")
    private Integer jerseyNumber;

    @OneToOne
    @JoinColumn(name = "profile_id", unique = true)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();

    public Player() {}

    public Player(String position, Integer jerseyNumber, Profile profile, Team team) {
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.profile = profile;
        this.team = team;
    }

    public Long getId() { return id; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public Integer getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(Integer jerseyNumber) { this.jerseyNumber = jerseyNumber; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public List<Attendance> getAttendances() { return attendances; }
}