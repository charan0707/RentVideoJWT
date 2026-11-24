
package com.rentvideo.service;

import com.rentvideo.dto.RegisterRequest;
import com.rentvideo.entity.AppUser;
import com.rentvideo.entity.Role;
import com.rentvideo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Email already registered"
            );
        }

        AppUser user = AppUser.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .firstName(request.firstName())
            .lastName(request.lastName())
            .role(request.role() == null ? Role.CUSTOMER : request.role())
            .build();

        return userRepository.save(user);
    }

    public AppUser getUserByEmail(String email) {
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            ));
}
}
