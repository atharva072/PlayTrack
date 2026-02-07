package com.project.playtrack.Player;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.playtrack.Team.Team;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByUsername (String username);

    Player findByEmail (String email);
    
    // get player with username and team
    Player findByUsernameAndTeam (String username, Team team);

    // get all players for a particular team
    List<Player> findByTeam_Name(String team);

    // to check if a player already exists for a team
    boolean existsByUsernameAndTeam_Name(String username, String team);
}
