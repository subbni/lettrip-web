package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.like.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked,Long> {
}
