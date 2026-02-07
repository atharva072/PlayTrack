package com.project.playtrack.Team;

import java.util.ArrayList;
import java.util.List;

import com.project.playtrack.Player.Player;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "team")
public class Team {
    @Id @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @ManyToOne
    @JoinColumn(name = "captain")  // FK to player.playerid
    private Player captain;

    @OneToMany(mappedBy = "team")
    private List<Player> players = new ArrayList<>();
    
    public Team (String name, Player captain, String city) {
        this.name = name;
        this.captain = captain;
        this.city = city;
    }

    // REQUIRED no-args constructor
    public Team() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getCaptain() {
        return captain;
    }

    public void setCaptain(Player captain) {
        this.captain = captain;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
