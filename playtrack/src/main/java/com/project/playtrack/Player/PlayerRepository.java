package com.project.playtrack.Player;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.playtrack.Team.Team;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByProfile_User_UserName (String username);
    
    // get player with username and team
    Player findByProfile_User_UserNameAndTeam (String username, Team team);

    // to check if a player already exists for a team
    boolean existsByProfile_User_UserNameAndTeam_Name(String username, String team);

    // ===========

    // "Profile" is the field in Player, "Username" is the field in Profile
    // boolean existsByProfile_User_UserName(String username);

    // Traverses from Player -> Team -> name
    // Player findByTeamName(String teamName);

    // If a specific player by their ID AND ensure they are on that team
    // boolean existsByIdAndTeamName(Long playerId, String teamName);

    // Returns all players where the associated team's name matches the input
    List<Player> findAllByTeamName(String teamName);

    // Safer version that ignores capital letters (e.g., "Lakers" vs "lakers")
    // List<Player> findAllByTeamNameIgnoreCase(String teamName);

    // Traverses: Player -> Profile -> User -> Username 
    // AND Player -> Team -> Name
    boolean existsByProfile_UserUserNameAndTeamName(String username, String teamName);
}
