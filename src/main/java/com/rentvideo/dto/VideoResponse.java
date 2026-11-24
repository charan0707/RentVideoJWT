package com.rentvideo.dto;

import com.rentvideo.entity.Video;

public record VideoResponse(
        Long id,
        String title,
        String director,
        String genre,
        boolean available
) {

    public static VideoResponse from(Video video) {
        return new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getDirector(),
                video.getGenre(),
                video.isAvailable()
        );
    }
}