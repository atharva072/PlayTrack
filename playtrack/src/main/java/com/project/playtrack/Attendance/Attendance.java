package com.project.playtrack.Attendance;

import java.time.LocalDate;

import com.project.playtrack.Player.Player;
import com.project.playtrack.Team.Team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "attendance",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"player_id", "team_id", "date"})
    }
)
public class Attendance {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendanceid")
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "present", nullable = false)
    private boolean present;
    
    public Attendance() {}

    public Long getId() { return this.id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public boolean getPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }
}