package com.project.playtrack.Profile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.playtrack.Util.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/create")
    public ApiResponse createProfile (@RequestBody ProfileDTO profileDTO) {
        return profileService.createProfile(profileDTO);
    }

    @GetMapping("path")
    public ApiResponse getMethodName(@RequestParam Long userId) {
        return profileService.getProfile(userId);
    }

    @PutMapping("path/{userId}")
    public ApiResponse putMethodName(@PathVariable Long userId, @RequestBody ProfileDTO profileDTO) {
        return profileService.updateProfile(userId, profileDTO);
    }
}
