package com.project.playtrack.User;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.playtrack.Util.ApiResponse;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // *************************************************
    // Registers a user
    // *************************************************
    public ApiResponse register (UserDTO userDTO) {
        // Validate username
        if (userRepository.existsByUserName(userDTO.getUserName())) return new ApiResponse("Registration failed", "Username already exists", null);

        // Validate email
        if (userRepository.existsByEmail(userDTO.getEmail())) return new ApiResponse("Registration failed", "Email already exists", null);

        // convert userDTO to user
        User user = convertToUser(userDTO);

        // save the user to the DB, if null return response to frontend
        User savedUser = saveUser(user);
        if (savedUser == null) return new ApiResponse("Registration failed", "There was an error in registering the user.", null);

        // else convert back to dto after save and prepare a response for the frontend
        userDTO = convertToDTO(savedUser);
        return new ApiResponse("User registered.", "", userDTO);
    }

    // *************************************************
    // Returns a user based on the username
    // *************************************************
    public User findByUsername(String username) {
        return userRepository.findByUserName(username); // .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    // *************************************************
    // checks if a username exists
    // *************************************************
    public ApiResponse checkUsername (String username) {
        System.out.println("username 2 : " + username);
        if (username == null || username.equals("")) {
            return new ApiResponse("ERROR", "Username cannot be empty", null);
        }
        boolean exists = userRepository.existsByUserName(username);
        System.out.println("exists 1 : " + exists);
        return new ApiResponse("SUCCESS", "", exists);
    }
    
    // *************************************************
    // Returns a user based on the username
    // *************************************************
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username); // .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    
    // *************************************************
    // Responsible for saving the user to DB
    // *************************************************
    @Transactional
    private User saveUser (User user) {
        try {
            if (user == null) return null;
            User savedUser = userRepository.save(user);
            System.out.println("User saved with ID: " + savedUser.getId());
            return savedUser;
        } catch (Exception ex) {
            return null;
        }
    }

    // *************************************************
    // Helper method for converting userDTO to User
    // *************************************************
    private User convertToUser (UserDTO userDTO) {
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setRoles(userDTO.getRoles());
        return user;
    }

    // *************************************************
    // Helper method for converting User to userDTO
    // *************************************************
    private UserDTO convertToDTO (User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUserName(user.getUsername());
        userDTO.setEmail(user.getEmail());

        Set<String> roles = new HashSet<>();
        roles.add(user.getAuthorities().iterator().next().getAuthority());
        userDTO.setRoles(roles);
        return userDTO;
    }
}
