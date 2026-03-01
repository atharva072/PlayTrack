package com.project.playtrack.Team;

import java.util.ArrayList;
import java.util.List;

import com.project.playtrack.Player.Player;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "teams")
public class Team {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "city", length = 100)
    private String city;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captain_id")
    private Player captain;

    @OneToMany(mappedBy = "team")
    private List<Player> players = new ArrayList<>();
    
    public Team() {}
    public Team (String name, String city) {
        this.name = name;
        this.city = city;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Player getCaptain() { return captain; }
    public void setCaptain(Player captain) { this.captain = captain; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public void addPlayers (Player player) {this.players.add(player); }
    public List<Player> getPlayers() { return players; }
}
