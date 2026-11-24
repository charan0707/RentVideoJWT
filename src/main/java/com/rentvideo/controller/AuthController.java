package com.rentvideo.controller;

import com.rentvideo.dto.AuthResponse;
import com.rentvideo.dto.LoginRequest;
import com.rentvideo.dto.RegisterRequest;
import com.rentvideo.dto.UserResponse;
import com.rentvideo.entity.AppUser;
import com.rentvideo.security.JwtService;
import com.rentvideo.service.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request) {

        AppUser user = userService.register(request);

        return UserResponse.from(user);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.email());

        String token = jwtService.generateToken(userDetails);

        AppUser user = userService.getUserByEmail(request.email());

        return new AuthResponse(
                token,
                "Bearer",
                UserResponse.from(user)
        );
    }
}