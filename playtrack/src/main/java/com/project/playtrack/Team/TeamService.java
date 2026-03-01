package com.project.playtrack.Team;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.project.playtrack.Player.Player;
import com.project.playtrack.Player.PlayerRepository;
import com.project.playtrack.Util.ApiResponse;

import jakarta.transaction.Transactional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    // *************************************************
    // Add a new team
    // *************************************************
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
            Team savedTeam = saveTeam(newTeam);
            if (savedTeam == null) {
                return new ApiResponse<>("error", "Team could not be saved.", null);
            }
            return new ApiResponse("success", "Team added successfully.", savedTeam);

        } catch (Exception ex) {
            return new ApiResponse("error", ex.getMessage(), null);
        }
    }

    // *************************************************
    // helper method to save team to DB
    // *************************************************
    @Transactional
    public Team saveTeam (Team team) {
        try {
            Team savedTeam = teamRepository.save(team);
            if (savedTeam == null) return null;
            return savedTeam;
        } catch (Exception ex) {
            return null;
        }
    }

    // *************************************************
    // Delete a team
    // *************************************************
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TeamDTO> removeTeam(String name, String city) {
        try {
            Team team = teamRepository.findByNameAndCity(name, city);
            if (team == null) {
                return new ApiResponse("ERROR", "Team does not exist so it cannot be deleted.", null);
            }

            // delete the team
            boolean performDelete = deleteTeam(team);
            
            // return the result of deletion
            if (performDelete) {
                return new ApiResponse<>("ERROR", "Deletion failed", null);
            } else {
                return new ApiResponse("SUCCESS", "Team deleted successfully.", null);
            }
        } catch (Exception ex) {
            return new ApiResponse("ERROR", "There was an error deleting team : " + ex.getMessage(), null);
        }
    }

    // *************************************************
    // helper method to delte team from DB
    // *************************************************
    @Transactional
    private boolean deleteTeam (Team team) {
        try {
            teamRepository.deleteById(team.getId());
            if (teamRepository.existsById(team.getId())) return false;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // *************************************************
    // Returns a team
    // *************************************************
    @PreAuthorize("hasRole('ADMIN', 'CAPTAIN', 'COACH', 'MANAGER')") @SuppressWarnings("null")
    public ApiResponse<Team> getTeam (String name, String city) {
        try {
            Team team = teamRepository.findByNameAndCity(name, city);
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

    // *************************************************
    // Returns all teams
    // *************************************************
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<List<TeamDTO>> getAllTeams() {
        try {
            List<TeamDTO> teamList = teamRepository.findAll().stream().map(this::convertToDTO).toList();
            for (TeamDTO teamDTO : teamList) {
                System.out.println("Team DTO: " + teamDTO.getName());
            }
            return new ApiResponse<>("success", "", teamList);
        } catch (Exception ex) {
            return new ApiResponse("error", ex.getMessage(), null);
        }
    }

    // *************************************************
    // Set team captain
    // *************************************************
    @PreAuthorize("hasAnyRole('ADMIN', 'CAPTAIN', 'COACH', 'MANAGER')")
    public ApiResponse<TeamDTO> setTeamCaptain (String name, String city, String username) {
        try {
            if (name == null || "".equals(name)) {
                return new ApiResponse("ERROR", "Please provide team name.", null);
            }

            if (username == null || "".equals(username)) {
                return new ApiResponse("ERROR", "Please provide player username.", null);
            }

            Team team = teamRepository.findByNameAndCity(name, city);
            if (team == null) {
                return new ApiResponse("ERROR", "This team does not exist.", null);
            }
            
            // convert string captain → Player entity
            Player captain = playerRepository.findByProfile_User_UserName(username);
            if (captain == null) {
                return new ApiResponse("ERROR", "The player does not exist", null);
            }

            // ensure the player belongs to this team
            if (!team.getPlayers().contains(captain)) {
                return new ApiResponse("ERROR", "Player does not belong to this team", null);
            }

            // if the current player is already the team captain
            if (team.getCaptain() == captain) {
                return new ApiResponse("ERROR", "This player is already captaining this team.", null);
            }

            // set the team captain
            team.setCaptain(captain);
            
            // save the team to the database
            Team savedTeam = saveTeam(team);
            if (savedTeam == null) {
                return new ApiResponse<>("error", "Save operation might have failed or returned an unexpected result.", null);
            }
            return new ApiResponse("success", "", null);

        } catch (Exception ex) {
            return new ApiResponse("success", ex.getMessage(), null);
        }
    }

    // *************************************************
    // Helper method to convert team to DTO
    // *************************************************
    private TeamDTO convertToDTO (Team team) {
        TeamDTO dto = new TeamDTO();
        dto.setName(team.getName());
        dto.setCity(team.getCity());
        return dto;
    }
}
