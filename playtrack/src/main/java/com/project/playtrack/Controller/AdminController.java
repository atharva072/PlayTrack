package com.project.playtrack.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.playtrack.DTO.TeamDTO;
import com.project.playtrack.Service.AttendanceService;
import com.project.playtrack.Service.PlayerService;
import com.project.playtrack.Service.TeamService;
import com.project.playtrack.Util.ApiResponse;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/dashboard")
    public Map<String, String> dashboard (Authentication auth) {
        return Map.of("message", "Welcome Admin", "user", auth.getName());
    }

    @GetMapping("/dashboard/totalTeams")
    public Integer getNumberOfTeams() {
        ApiResponse<List<TeamDTO>> list = teamService.getAllTeams();
        return list.getData().size();
    }

    @GetMapping("/dashboard/totalPlayers")
    public Long getNumberOfPlayers() {
        ApiResponse<Long> count = playerService.getAllPlayers();
        return count.getData();
    }

    @GetMapping("/dashboard/totalAttendances")
    public Long getNumberOfAttendances() {
        ApiResponse<Long> count = attendanceService.totalAttendances();
        return count.getData();
    }

    @GetMapping("/teams")
    public ApiResponse<List<TeamDTO>> getAllTeams() {
        return teamService.getAllTeams();
    }
}
