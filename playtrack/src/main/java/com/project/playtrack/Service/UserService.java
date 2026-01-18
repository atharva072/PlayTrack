package com.project.playtrack.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.playtrack.DTO.UserDTO;
import com.project.playtrack.Entity.Player;
import com.project.playtrack.Entity.User;
import com.project.playtrack.Enum.Role;
import com.project.playtrack.Repository.PlayerRepository;
import com.project.playtrack.Repository.UserRepository;
import com.project.playtrack.Util.ApiResponse;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public ApiResponse register (UserDTO userDTO) {
        // validations
        // Validate username
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            return new ApiResponse("Registration failed", "Username already exists", null);
        }

        // Validate email
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return new ApiResponse("Registration failed", "Email already exists", null);
        }

        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setPlayer(userDTO.isPlayer());
        user.setRole(Role.valueOf(userDTO.getRole()));

        // if the current user is a player, add a link in the DB between the 2 entities
        if (user.isPlayer()) {
            Player player = playerRepository.findByUsername(user.getUserName());
            if (player != null) {
                player.setUser(user);
                player.setUsername(user.getUserName());
                playerRepository.save(player);
            } else {
                return new ApiResponse("Registration failed", "Please ask you captain or admin to create your profile for your team.", null);
            }
        }

        // save the user to the DB
        User saved = userRepository.save(user);

        // prepare a response to save
        UserDTO response = new UserDTO();
        response.setUserId(saved.getUserId());
        response.setUserName(saved.getUsername());
        response.setEmail(saved.getEmail());
        response.setPlayer(saved.isPlayer());
        response.setRole(saved.getRole().name());
        return new ApiResponse("User registered.", "", response);
    }

    public User findByUsername(String username) {
        return userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
