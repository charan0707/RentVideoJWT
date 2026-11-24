package com.rentvideo.dto;

import com.rentvideo.entity.Rental;

import java.time.LocalDateTime;

public record RentalResponse(
        Long id,
        Long videoId,
        String videoTitle,
        LocalDateTime rentedAt,
        LocalDateTime returnedAt
) {

    public static RentalResponse from(Rental rental) {
        return new RentalResponse(
                rental.getId(),
                rental.getVideo().getId(),
                rental.getVideo().getTitle(),
                rental.getRentedAt(),
                rental.getReturnedAt()
        );
    }
}