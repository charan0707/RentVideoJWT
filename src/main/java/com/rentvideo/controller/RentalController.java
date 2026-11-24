package com.rentvideo.controller;

import com.rentvideo.dto.RentalResponse;
import com.rentvideo.service.RentalService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping("/{videoId}/rent")
    public RentalResponse rentVideo(
            @PathVariable Long videoId,
            @AuthenticationPrincipal UserDetails userDetails) {

        return rentalService.rentVideo(
                videoId,
                userDetails.getUsername()
        );
    }

    @PostMapping("/{videoId}/return")
    public RentalResponse returnVideo(
            @PathVariable Long videoId,
            @AuthenticationPrincipal UserDetails userDetails) {

        return rentalService.returnVideo(
                videoId,
                userDetails.getUsername()
        );
    }
}