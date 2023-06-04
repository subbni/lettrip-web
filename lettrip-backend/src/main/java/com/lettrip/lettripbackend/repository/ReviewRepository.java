package com.lettrip.lettripbackend.repository;


import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.domain.travel.Review;
import com.lettrip.lettripbackend.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByUserAndPlace(User user, Place place);
    Page<Review> findByPlace(Place place, Pageable pageable);
}
