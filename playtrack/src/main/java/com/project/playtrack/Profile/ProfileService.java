package com.project.playtrack.Profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.playtrack.User.User;
import com.project.playtrack.User.UserRepository;
import com.project.playtrack.Util.ApiResponse;

import jakarta.transaction.Transactional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepo;

    @Autowired
    private UserRepository userRepository;

    private User user;

    // *************************************************
    // Creates a profile
    // *************************************************
    public ApiResponse createProfile (ProfileDTO profileDTO) {
        if (profileDTO.getUsername() == null || profileDTO.getUsername().equals("")) {
            return new ApiResponse("ERROR", "There was an error in creating the profile. Please provide the username.", null);
        }

        user = userRepository.findByUserName(profileDTO.getUsername());
        System.out.println("user: " + user.getEmail() + ", " + user.getUserName());
        if (user == null) return new ApiResponse("ERROR", "There was an error in creating the profile. No user found with this username. Please create your user first", null);

        Profile profile = convertToProfile(profileDTO);
        Profile savedProfile = saveProfile(profile);
        if (savedProfile == null) {
            return new ApiResponse("ERROR", "There was an error in creating the profile.", null);
        }
        
        profileDTO = convertToProfileDTO(savedProfile);
        return new ApiResponse("User registered.", "", profileDTO);
    }

    // *************************************************
    // Returns a profile based on userId
    // *************************************************
    public ApiResponse getProfile (Long userId) {
        Profile profile = profileRepo.findByUserId(userId);
        if (profile == null) {
            return new ApiResponse("ERROR", "Profile for " + userId + " was not found.", null);
        }
        return new ApiResponse("SUCCESS", "Profile for " + userId + " was found.", profile);
    }
    
    // *************************************************
    // For updating profile
    // *************************************************
    public ApiResponse updateProfile (Long userId, ProfileDTO profileDTO) {
        Profile profile = (Profile) getProfile(userId).getData();
        if (profile == null) {
            return new ApiResponse("ERROR", "Profile for " + userId + " was not found.", null);
        }

        if (profileDTO.getCity() != null) {
            profile.setCity(profileDTO.getCity());
        }
        if (profileDTO.getDesignation() != null) {
            profile.setDesignation(profileDTO.getDesignation());
        }
        if (profileDTO.getFirstName() != null) {
            profile.setFirstName(profileDTO.getFirstName());
        }
        if (profileDTO.getLastName() != null) {
            profile.setLastName(profileDTO.getLastName());
        }
        if (profileDTO.getPhone() != null) {
            profile.setPhone(profileDTO.getPhone());
        }
        if (profileDTO.getType() != null) {
            profile.setType(profileDTO.getType());
        }
        
        Profile updatedProfile = saveProfile(profile);
        if (updatedProfile == null) {
            return new ApiResponse("ERROR", "Profile update " + userId + " failed.", null);
        }
        return new ApiResponse("SUCCESS", "Profile updated for " + userId + ".", updatedProfile);
    }
    
    // *************************************************
    // Saves profile to the DB
    // *************************************************
    @Transactional
    private Profile saveProfile (Profile profile) {
        try {
            if (profile == null) return null;
            Profile savedProfile = profileRepo.save(profile);
            return savedProfile;
        } catch (Exception ex) {
            return null;
        }
    }
    
    // *************************************************
    // Converts DTO to Profile
    // *************************************************
    private Profile convertToProfile (ProfileDTO profileDTO) {
        Profile profile = new Profile();
        profile.setCity(profileDTO.getCity());
        profile.setDesignation(profileDTO.getDesignation());
        profile.setFirstName(profileDTO.getFirstName());
        profile.setLastName(profileDTO.getLastName());
        profile.setPhone(profileDTO.getPhone());
        profile.setUser(user);
        profile = assignProfileType(profile, profileDTO);
        return profile;
    }

    // *************************************************
    // Converts Profile to DTO
    // *************************************************
    private ProfileDTO convertToProfileDTO (Profile profile) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setCity(profile.getCity());
        profileDTO.setDesignation(profile.getDesignation());
        profileDTO.setFirstName(profile.getFirstName());
        profileDTO.setLastName(profile.getLastName());
        profileDTO.setPhone(profile.getPhone());
        profileDTO.setType(profile.getType());
        profileDTO.setUsername(profile.getUser().getUserName());
        return profileDTO;
    }

    // *************************************************
    // Sets profile types
    // *************************************************
    private Profile assignProfileType (Profile profile, ProfileDTO profileDTO) {
        if (null == profileDTO.getType()) {
            profile.setType(profileDTO.getType());
        } else switch (profileDTO.getType()) {
            case CAPTAIN -> profile.setType(ProfileType.CAPTAIN_PENDING);
            case COACH -> profile.setType(ProfileType.COACH_PENDING);
            default -> profile.setType(profileDTO.getType());
        }
        return profile;
    }
}
