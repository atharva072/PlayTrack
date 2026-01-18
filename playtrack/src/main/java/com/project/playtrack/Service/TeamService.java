package com.project.playtrack.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.project.playtrack.DTO.TeamDTO;
import com.project.playtrack.Entity.Player;
import com.project.playtrack.Entity.Team;
import com.project.playtrack.Repository.PlayerRepository;
import com.project.playtrack.Repository.TeamRepository;
import com.project.playtrack.Util.ApiResponse;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    // 1. Add team
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Team> addTeam(TeamDTO teamDTO) {

        // if team has no name || city return the error message
        if (teamDTO.getName() == null || "".equals(teamDTO.getName())) {
            return new ApiResponse("error", "Please provide a name for your team.", null);
        }

        if (teamDTO.getCity() == null || "".equals(teamDTO.getCity())) {
            return new ApiResponse("error", "Please provide a city name from where your team belongs.", null);
        }

        // check if team is already present in the DB
        Team team = teamRepository.findByNameAndCity(teamDTO.getName(), teamDTO.getCity());
        if (team != null) {
            return new ApiResponse("error", "This team already exists.", null);
        }

        try {
            Team newTeam = new Team();
            newTeam.setName(teamDTO.getName());
            newTeam.setCity(teamDTO.getCity());

            // save the team to the database
            Team savedTeam = teamRepository.save(newTeam);

            // check the save result and return an appropriate response
            if (savedTeam != null && !"".equals(savedTeam.getName())) {
                return new ApiResponse("success", "Team added successfully.", savedTeam);
            } else {
                return new ApiResponse<>("error", "Save operation might have failed or returned an unexpected result.", null);
            }
        } catch (Exception ex) {
            return new ApiResponse("error", ex.getMessage(), null);
        }
    }

    // 2. Delete team
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TeamDTO> deleteTeam(String name) {
        try {

            Team team = teamRepository.findById(name).orElse(null);
            if (team == null) {
                return new ApiResponse("success", "Team does not exist.", null);
            }

            // delete the team
            teamRepository.deleteById(name);
            
            // return the result of deletion
            if (teamRepository.existsById(name)) {
                return new ApiResponse<>("error", "Deletion failed", null);
            } else {
                return new ApiResponse("success", "Team deleted successfully.", null);
            }
        } catch (Exception ex) {
            return new ApiResponse("error", "There was an error deleting team : " + ex.getMessage(), null);
        }
    }

    // 3. Get a team by name
    @PreAuthorize("hasRole('ADMIN')") @SuppressWarnings("null")
    public ApiResponse<Team> getTeam(String name) {
        try {
            Team team = teamRepository.findById(name).orElse(null);
            if (team != null) {
                TeamDTO teamDTO = convertToDTO(team);
                return new ApiResponse("success", "Team found", teamDTO);
            } else {
                return new ApiResponse("error", "Team named '" + name  + "' does not exist.", null);
            }
        } catch (Exception ex) {
            return new ApiResponse("error", ex.getMessage(), null);
        }
    }

    // 4. Get all teams
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<List<TeamDTO>> getAllTeams() {
        try {
            List<TeamDTO> teamList = teamRepository.findAll().stream().map(this::convertToDTO).toList();
            return new ApiResponse("success", "", teamList);
        } catch (Exception ex) {
            return new ApiResponse("error", ex.getMessage(), null);
        }
    }

    // 5. Set team captain
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN')")
    public ApiResponse<TeamDTO> setTeamCaptain (String name, String username) {
        try {
            if (name == null || "".equals(name)) {
                return new ApiResponse("error", "Please provide team name.", null);
            }

            if (username == null || "".equals(username)) {
                return new ApiResponse("error", "Please provide player username.", null);
            }

            Team team = teamRepository.findById(name).orElse(null);
            if (team == null) {
                return new ApiResponse("error", "This team does not exist.", null);
            }
            
            // convert string captain → Player entity
            Player captain = playerRepository.findByUsername(username);
            if (captain == null) {
                return new ApiResponse("error", "The player does not exist", null);
            }

            // ensure the player belongs to this team
            if (!team.getPlayers().contains(captain)) {
                return new ApiResponse("error", "Player does not belong to this team", null);
            }

            // if the current player is already the team captain
            if (team.getCaptain() == captain) {
                return new ApiResponse("error", "This player is already captaining this team.", null);
            }

            // set the team captain
            team.setCaptain(captain);
            
            // save the team to the database
            Team savedTeam = teamRepository.save(team);

            // check the save result and return an appropriate response
            if (savedTeam != null && !"".equals(savedTeam.getName())) {
                return new ApiResponse("success", captain.getFullName() + " is now the captain of " + team.getName(), null);
            } else {
                return new ApiResponse<>("error", "Save operation might have failed or returned an unexpected result.", null);
            }
        } catch (Exception ex) {
            return new ApiResponse("success", ex.getMessage(), null);
        }
    }

    // HELPER Functions
    private TeamDTO convertToDTO(Team team) {
        TeamDTO dto = new TeamDTO();
        dto.setName(team.getName());
        dto.setCity(team.getCity());
        dto.setCaptain(team.getCaptain() != null ? team.getCaptain().getUsername() : null);

        List<String> playerNames = team.getPlayers().stream()
                .map(Player::getUsername)
                .toList();

        dto.setPlayers(playerNames);
        return dto;
    }
}
