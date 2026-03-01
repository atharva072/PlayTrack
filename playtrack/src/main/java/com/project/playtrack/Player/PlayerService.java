package com.project.playtrack.Player;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.project.playtrack.Team.Team;
import com.project.playtrack.Team.TeamRepository;
import com.project.playtrack.Util.ApiResponse;

import jakarta.transaction.Transactional;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerValidation playerValidation;

    // *************************************************
    // Adds a player
    // *************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<PlayerDTO> addPlayer(PlayerDTO dto) {
        
        // perform validations
        if (!playerValidation.validateAddPlayer(dto)) {
            return playerValidation.getResponse();
        }
        Player player = convertDtoToPlayer(dto);

        try {
            // add the player to the DB
            Player savedPlayer = savePlayer(player);
            if (player == null) {
                return new ApiResponse("error", "Save operation might have failed or returned an unexpected result.", null);
            }
            return new ApiResponse("success", dto.getFirstName() + " " + dto.getLastName() + " was added to " + dto.getTeamName(), savedPlayer);

        } catch (Exception ex) {
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }

    // *************************************************
    // Helper method to save player
    // *************************************************
    @Transactional
    private Player savePlayer (Player player) {
        try {
            if (player == null) return null;
            Player savedPlayer = playerRepository.save(player);
            return savedPlayer;
        } catch (Exception ex) {
            return null;
        }
    }

    // *************************************************
    // Searches a player based on username in the DB
    // *************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<PlayerDTO> searchPlayer(String username) {
        try {
            Player player = playerRepository.findByProfile_User_UserName(username);
            if (player == null) {
                return new ApiResponse("ERROR", "Could not find player with username " + username, null);
            }
            return new ApiResponse("SUCCESS", "Could not find player with username " + username, player);

        } catch (Exception ex) {
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }

    // *************************************************
    // Returns all players for a team
    // *************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<List<PlayerDTO>> getAllPlayers(String team) {
        try {
            List<PlayerDTO> playersDTOList = new ArrayList<>();

            // get all the players for the team
            List<Player> playersList = new ArrayList<>(playerRepository.findAllByTeamName(team));
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

    // *************************************************
    // Returns total player count
    // *************************************************
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<Long> getPlayerCount() {
        try {
            return new ApiResponse<>("success", "", playerRepository.count());
        } catch (Exception ex) {
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }

    // *************************************************
    // Deletes a player
    // *************************************************
    @Transactional @PreAuthorize("hasRole('ADMIN')") @SuppressWarnings("null")
    public ApiResponse<PlayerDTO> removePlayer(String teamName, String username) {
        try {
            // check if the player is part of the team, if not -> cannot delete
            if (!playerRepository.existsByProfile_UserUserNameAndTeamName(username, teamName)) {
                return new ApiResponse<>("error", "This player does not exist in this team.", null);
            }

            // get the team object, if team is not in database -> wrong team
            Team teamObj = teamRepository.findByName(teamName);
            if (teamObj == null) {
                return new ApiResponse<>("error", "Deletion failed. Cannot find team: " + teamName, null);
            } 
            
            // get the player with the username from that team -> if not present return error message
            else {
                Player player = playerRepository.findByProfile_User_UserNameAndTeam(username, teamObj);
                if (player == null) {
                    return new ApiResponse<>("error", "Deletion failed. Cannot find player with username: " + username +  " in team: " + teamName, null);
                }

                // if we are here after all the checks, perform the deletion
                playerRepository.deleteById(player.getId());

                // return the result of deletion
                if (playerRepository.existsById(player.getId())) {
                    return new ApiResponse<>("error", "Deletion failed", null);
                } else {
                    return new ApiResponse<>("success", "", null);
                }
            }
        } catch (Exception ex) {
            return new ApiResponse<>("error", ex.getMessage(), null);
        }
    }

    // *************************************************
    // Helper method to convert player to dto
    // *************************************************
    private PlayerDTO convertPlayerToDTO (Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setFirstName(player.getProfile().getFirstName());
        dto.setLastName(player.getProfile().getLastName());
        dto.setJerseyNumber(player.getJerseyNumber());
        dto.setPosition(player.getPosition());
        dto.setTeamName(player.getTeam().getName());
        return dto;
    }
    
    // *************************************************
    // Helper method to convert dto to player
    // *************************************************
    private Player convertDtoToPlayer (PlayerDTO playerDTO) {
        Player player = new Player();
        player.setPosition(playerDTO.getPosition());
        player.setJerseyNumber(playerDTO.getJerseyNumber());
        player.setTeam(playerValidation.getTeam());
        return player;
    }
}
