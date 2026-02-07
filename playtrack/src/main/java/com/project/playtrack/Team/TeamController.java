package com.project.playtrack.Team;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.playtrack.Util.ApiResponse;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    // Create a new team
    @PostMapping("/add")
    public ApiResponse<Team> addTeam(@RequestBody TeamDTO teamDTO) {
        return teamService.addTeam(teamDTO);
    }

    // Get a team by name
    @GetMapping("/{name}")
    public ApiResponse<Team> getTeam(@PathVariable String name) {
        return teamService.getTeam(name);
    }

    // Get all teams
    @GetMapping("/all")
    public ApiResponse<List<TeamDTO>> getAllTeams() {
        return teamService.getAllTeams();
    }

    // set team captain
    @PatchMapping("/{name}/captain/{username}")
    public ApiResponse<TeamDTO> setTeamCaptain(@PathVariable String name, @PathVariable String username) {
        return teamService.setTeamCaptain(name, username);
    }

    // Delete a team by name
    @DeleteMapping("/{name}")
    public ApiResponse<TeamDTO> deleteTeam(@PathVariable String name) {
        return teamService.deleteTeam(name);
    }
}
