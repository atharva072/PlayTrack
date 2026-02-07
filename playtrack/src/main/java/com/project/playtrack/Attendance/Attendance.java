package com.project.playtrack.Attendance;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.playtrack.Player.Player;
import com.project.playtrack.Team.Team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendanceid")
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "team", nullable = false)  // references team.name
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player", nullable = false)  // references player.playerid
    @JsonBackReference
    private Player player;

    @Column(name = "present", nullable = false)
    private boolean present;
    
    public Attendance() {}

    public Long getId() {
        return this.id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean getPresent() {
        return present;
    }
    
    public void setPresent(boolean present) {
        this.present = present;
    }
}
