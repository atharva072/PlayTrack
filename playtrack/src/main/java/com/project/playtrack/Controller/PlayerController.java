package com.project.playtrack.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.playtrack.DTO.PlayerDTO;
import com.project.playtrack.Service.PlayerService;
import com.project.playtrack.Util.ApiResponse;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/add")
    public ApiResponse<PlayerDTO> addPlayer(@RequestBody PlayerDTO playerDTO) {
        return playerService.addPlayer(playerDTO);
    }

    @GetMapping("/{username}")
    public ApiResponse<PlayerDTO> getPlayer(@PathVariable String username) {
        return playerService.searchPlayer(username);
    }

    @GetMapping("/{team}/all")
    public ApiResponse<List<PlayerDTO>> getAllPlayers(@PathVariable String team) {
        return playerService.getAllPlayers(team);
    }

    @DeleteMapping("/{team}/{username}")
    public ApiResponse<PlayerDTO> removePlayer(@PathVariable String team, @PathVariable String username) {
        return playerService.removePlayer(team, username);
    }
}
