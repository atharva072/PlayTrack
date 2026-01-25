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
import com.project.playtrack.Validations.PlayerValidation;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerValidation playerValidation;


    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<PlayerDTO> addPlayer(PlayerDTO dto) {
        
        // perform validations
        if (!playerValidation.validateAddPlayer(dto)) {
            return playerValidation.getResponse();
        }

        // after all the checks, create a new Player entity from DTO
        Player player = convertDtoToPlayer(dto);

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
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }    

    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<PlayerDTO> searchPlayer(String username) {
        try {
            // perform validations
            if (!playerValidation.validateSearchPlayer(username)) {
                return playerValidation.getResponse();
            }

            // else convert the DTO object to player
            PlayerDTO p = convertPlayerToDTO(playerValidation.getPlayer());
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
                    PlayerDTO p = convertPlayerToDTO(player);
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

    // HELPER METHODS

    private PlayerDTO convertPlayerToDTO (Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setFirstName(player.getFirstName());
        dto.setLastName(player.getLastName());
        dto.setUsername(player.getUsername());
        dto.setEmail(player.getEmail());
        dto.setJerseyNumber(player.getJerseyNumber());
        dto.setPosition(player.getPosition());
        dto.setRole(player.getRole());
        dto.setTeam(player.getTeam().getName());
        return dto;
    }

    private Player convertDtoToPlayer (PlayerDTO playerDTO) {
        Player player = new Player();
        player.setUsername(playerDTO.getUsername());
        player.setFirstName(playerDTO.getFirstName());
        player.setLastName(playerDTO.getLastName());
        player.setEmail(playerDTO.getEmail());
        player.setPosition(playerDTO.getPosition());
        player.setRole(playerDTO.getRole());
        player.setJerseyNumber(playerDTO.getJerseyNumber());
        player.setTeam(playerValidation.getTeam());
        return player;
    }
}
