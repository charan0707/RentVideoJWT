package com.rentvideo.service;

import com.rentvideo.dto.RentalResponse;
import com.rentvideo.entity.AppUser;
import com.rentvideo.entity.Rental;
import com.rentvideo.entity.Video;
import com.rentvideo.repository.RentalRepository;
import com.rentvideo.repository.UserRepository;
import com.rentvideo.repository.VideoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RentalService {

    private static final int MAX_ACTIVE_RENTALS = 2;

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @Transactional
    public RentalResponse rentVideo(Long videoId, String email) {

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User does not exist"
                ));

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Video does not exist"
                ));

        if (!video.isAvailable()
                || rentalRepository.existsByVideo_IdAndReturnedAtIsNull(videoId)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Video is already rented"
            );
        }

        long activeRentals =
                rentalRepository.countByUser_IdAndReturnedAtIsNull(user.getId());

        if (activeRentals >= MAX_ACTIVE_RENTALS) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot rent more than 2 videos at a time"
            );
        }

        Rental rental = Rental.builder()
                .user(user)
                .video(video)
                .rentedAt(LocalDateTime.now())
                .build();

        video.setAvailable(false);
        videoRepository.save(video);

        Rental savedRental = rentalRepository.save(rental);

        return RentalResponse.from(savedRental);
    }

    @Transactional
    public RentalResponse returnVideo(Long videoId, String email) {

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User does not exist"
                ));

        Rental rental = rentalRepository
                .findByUser_IdAndVideo_IdAndReturnedAtIsNull(
                        user.getId(), videoId
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No active rental found for this user and video"
                ));

        rental.setReturnedAt(LocalDateTime.now());

        Video video = rental.getVideo();
        video.setAvailable(true);

        videoRepository.save(video);

        Rental savedRental = rentalRepository.save(rental);

        return RentalResponse.from(savedRental);
    }
}