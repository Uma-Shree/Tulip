package com.example.TulipApplication.controllers;

import com.example.TulipApplication.entities.User;
import com.example.TulipApplication.repositories.UserRepository;
import com.example.TulipApplication.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // 1. REGISTER USER
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        // Encrypt the password before saving!
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    // 2. LOGIN USER
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        // This validates the username and password automatically
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        // If validation passed, generate the token
        String token = jwtUtil.generateToken(user.getUsername());

        // Return the token to the user
        return Map.of("token", token);
    }
}