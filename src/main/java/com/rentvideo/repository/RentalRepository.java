package com.rentvideo.repository;

import com.rentvideo.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    long countByUser_IdAndReturnedAtIsNull(Long userId);

    boolean existsByVideo_IdAndReturnedAtIsNull(Long videoId);

    Optional<Rental> findByUser_IdAndVideo_IdAndReturnedAtIsNull(
            Long userId, Long videoId);
}