package com.rentvideo.controller;

import com.rentvideo.dto.VideoRequest;
import com.rentvideo.dto.VideoResponse;
import com.rentvideo.service.VideoService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public List<VideoResponse> getAvailableVideos() {

        return videoService.getAvailableVideos()
                .stream()
                .map(VideoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public VideoResponse getVideo(@PathVariable Long id) {

        return VideoResponse.from(
                videoService.getVideoById(id)
        );
    }

    @PostMapping
    public VideoResponse createVideo(
            @Valid @RequestBody VideoRequest request) {

        return VideoResponse.from(
                videoService.createVideo(request)
        );
    }

    @PutMapping("/{id}")
    public VideoResponse updateVideo(
            @PathVariable Long id,
            @Valid @RequestBody VideoRequest request) {

        return VideoResponse.from(
                videoService.updateVideo(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public String deleteVideo(@PathVariable Long id) {

        videoService.deleteVideo(id);

        return "Video deleted successfully";
    }
}