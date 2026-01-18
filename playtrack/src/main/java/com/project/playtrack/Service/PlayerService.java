package com.project.playtrack.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.project.playtrack.DTO.PlayerDTO;
import com.project.playtrack.Entity.Player;
import com.project.playtrack.Entity.Team;
import com.project.playtrack.Repository.PlayerRepository;
import com.project.playtrack.Repository.TeamRepository;
import com.project.playtrack.Util.ApiResponse;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<PlayerDTO> addPlayer(PlayerDTO dto) {

        // validate team input
        if (dto.getTeam() == null || dto.getTeam().isEmpty()) {
            return new ApiResponse<>("error", "Team is required for adding a player.", null);
        }

        // The team should be present in the DB before adding the player
        @SuppressWarnings("null")
        Team team = teamRepository.findById(dto.getTeam()).orElse(null);
        if (team == null) {
            return new ApiResponse<>("error", "Team not found: " + dto.getTeam(), null);
        }

        if (dto.getUsername() == null || "".equals(dto.getUsername())) {
            return new ApiResponse<>("error", "Please provide username of the player." + dto.getTeam(), null);
        }

        if (dto.getEmail() == null || "".equals(dto.getEmail())) {
            return new ApiResponse<>("error", "Please provide email address of the player." + dto.getTeam(), null);
        }

        // if a player is already in the team don't add him again
        boolean playerExists = playerRepository.existsByUsernameAndTeam_Name(dto.getUsername(), dto.getTeam());
        if (playerExists) {
            return new ApiResponse<>("error", "This player already exists in this team.", null);
        }

        // after all the checks, create a new Player entity from DTO
        Player player = new Player();
        player.setUsername(dto.getUsername());
        player.setFirstName(dto.getFirstName());
        player.setLastName(dto.getLastName());
        player.setEmail(dto.getEmail());
        player.setPosition(dto.getPosition());
        player.setRole(dto.getRole());
        player.setJerseyNumber(dto.getJerseyNumber());
        player.setTeam(team);

        try {
            // add the player to the DB
            Player savedPlayer = playerRepository.save(player);

            // check the result and return an appropriate response
            if (savedPlayer != null && savedPlayer.getId() != null) {
                return new ApiResponse<>("success", dto.getFirstName() + " " + dto.getLastName() + " was added to " + dto.getTeam(), null);
            } else {
                return new ApiResponse<>("error", "Save operation might have failed or returned an unexpected result.", null);
            }
        } catch (Exception ex) {
            return new ApiResponse<>("error", "This player already exists in this team.", null);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<PlayerDTO> searchPlayer(String username) {
        try {

            // search for the player in the DB, if not found inform frontend
            Player player = playerRepository.findByUsername(username);
            if (player == null) {
                return new ApiResponse<>("error", "Player does not exist.", null);
            }

            // else convert the DTO object to player
            PlayerDTO p = new PlayerDTO();
            p.setId(player.getId());
            p.setFirstName(player.getFirstName());
            p.setLastName(player.getLastName());
            p.setUsername(player.getUsername());
            p.setEmail(player.getEmail());
            p.setJerseyNumber(player.getJerseyNumber());
            p.setPosition(player.getPosition());
            p.setRole(player.getRole());
            p.setTeam(player.getTeam().getName());
            return new ApiResponse<>("success", "", p);
        } catch (Exception ex) {
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<List<PlayerDTO>> getAllPlayers(String team) {
        try {
            List<PlayerDTO> playersDTOList = new ArrayList<>();

            // get all the players for the team
            List<Player> playersList = new ArrayList<>(playerRepository.findByTeam_Name(team));
            if (!playersList.isEmpty()) {
                for (Player player : playersList) {
                    PlayerDTO p = new PlayerDTO();
                    p.setId(player.getId());
                    p.setFirstName(player.getFirstName());
                    p.setLastName(player.getLastName());
                    p.setUsername(player.getUsername());
                    p.setEmail(player.getEmail());
                    p.setJerseyNumber(player.getJerseyNumber());
                    p.setPosition(player.getPosition());
                    p.setRole(player.getRole());
                    p.setTeam(player.getTeam().getName());
                    playersDTOList.add(p);
                }
            }
            return new ApiResponse<>("success", "", playersDTOList);
        } catch (Exception ex) {
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')") @SuppressWarnings("null")
    public ApiResponse<PlayerDTO> removePlayer(String team, String username) {
        try {
            // check if the player is part of the team, if not -> cannot delete
            if (!playerRepository.existsByUsernameAndTeam_Name(username, team)) {
                return new ApiResponse<>("error", "This player does not exist in this team.", null);
            }

            // get the team object, if team is not in database -> wrong team
            Team teamObj = teamRepository.findById(team).orElse(null);
            if (teamObj == null) {
                return new ApiResponse<>("error", "Deletion failed. Cannot find team: " + team, null);
            } 
            
            // get the player with the username from that team -> if not present return error message
            else {
                Player player = playerRepository.findByUsernameAndTeam(username, teamObj);
                if (player == null) {
                    return new ApiResponse<>("error", "Deletion failed. Cannot find player with username: " + username +  " in team: " + team, null);
                }

                // if we are here after all the checks, perform the deletion
                playerRepository.deleteById(player.getId());

                // return the result of deletion
                if (playerRepository.existsById(player.getId())) {
                    return new ApiResponse<>("error", "Deletion failed", null);
                } else {
                    return new ApiResponse<>("success", player.getFullName() + " was removed from " + player.getTeam().getName(), null);
                }
            }
        } catch (Exception ex) {
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }
}
