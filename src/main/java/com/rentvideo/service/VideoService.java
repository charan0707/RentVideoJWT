package com.rentvideo.service;

import com.rentvideo.dto.VideoRequest;
import com.rentvideo.entity.Video;
import com.rentvideo.repository.VideoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    public List<Video> getAvailableVideos() {
        return videoRepository.findByAvailableTrue();
    }

    public Video getVideoById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Video with id " + id + " does not exist"
                ));
    }

    public Video createVideo(VideoRequest request) {

        Video video = Video.builder()
                .title(request.title())
                .director(request.director())
                .genre(request.genre())
                .available(true)
                .build();

        return videoRepository.save(video);
    }

    public Video updateVideo(Long id, VideoRequest request) {

        Video video = getVideoById(id);

        video.setTitle(request.title());
        video.setDirector(request.director());
        video.setGenre(request.genre());

        if (request.available() != null) {
            video.setAvailable(request.available());
        }

        return videoRepository.save(video);
    }

    public void deleteVideo(Long id) {

        Video video = getVideoById(id);
        videoRepository.delete(video);
    }
}