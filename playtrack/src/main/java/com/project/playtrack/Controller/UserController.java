package com.project.playtrack.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.playtrack.DTO.LoginRequestDTO;
import com.project.playtrack.DTO.UserDTO;
import com.project.playtrack.Entity.User;
import com.project.playtrack.Service.UserService;
import com.project.playtrack.Util.ApiResponse;
import com.project.playtrack.Util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ApiResponse register(@RequestBody UserDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userService.register(dto);
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        String token = jwtUtil.generateToken(user);
        return new ApiResponse("Login successful", "", token);
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        return "Welcome Admin!";
    }
}