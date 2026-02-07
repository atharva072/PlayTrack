package com.project.playtrack.Validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.playtrack.Player.Player;
import com.project.playtrack.Player.PlayerDTO;
import com.project.playtrack.Player.PlayerRepository;
import com.project.playtrack.Team.Team;
import com.project.playtrack.Team.TeamRepository;
import com.project.playtrack.Util.ApiResponse;

@Component
public class PlayerValidation {

    @Autowired 
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private boolean isValid;
    private ApiResponse response;
    private Team team;
    private Player player;

    public boolean isValid () {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public ApiResponse getResponse() {
        return response;
    }

    public Team getTeam() {
        return team;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean validateAddPlayer (PlayerDTO dto) {

        // validate team input
        if (dto.getTeam() == null || dto.getTeam().isEmpty()) {
            setIsValid(false);
            this.response = new ApiResponse<>("error", "Team is required for adding a player.", null);
            return false;
        } else {
            // The team should be present in the DB before adding the player
            Team team = teamRepository.findById(dto.getTeam()).orElse(null);
            if (team == null) {
                setIsValid(false);
                this.response = new ApiResponse<>("error", "Team not found: " + dto.getTeam(), null);
                return false;
            } else {
                System.out.println("team : " + team.getName());
                this.team = team;
            }
        }

        if (dto.getUsername() == null || "".equals(dto.getUsername())) {
            setIsValid(false);
            this.response = new ApiResponse<>("error", "Please provide username of the player.", null);
            return false;
        }

        if (dto.getEmail() == null || "".equals(dto.getEmail())) {
            setIsValid(false);
            this.response = new ApiResponse<>("error", "Please provide email address of the player.", null);
            return false;
        }

        // if a player is already in the team don't add him again
        boolean playerExists = playerRepository.existsByUsernameAndTeam_Name(dto.getUsername(), dto.getTeam());
        if (playerExists) {
            setIsValid(false);
            this.response = new ApiResponse<>("error", "This player already exists in this team.", null);
            return false;
        }
        return true;
    }

    public boolean validateSearchPlayer (String username) {
        // search for the player in the DB, if not found inform frontend
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            setIsValid(false);
            this.response = new ApiResponse<>("error", "Player does not exist.", null);
            return false;
        } else {
            this.player = player;
        }
        return true;
    }
}
