package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.travel.Place;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByLocationPoint(Point locationPoint);
}
