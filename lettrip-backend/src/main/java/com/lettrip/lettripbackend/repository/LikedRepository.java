package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.liked.Liked;
import com.lettrip.lettripbackend.constant.LikedType;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked,Long>, JpaSpecificationExecutor<Liked> {
    Page<Liked> findAll(@Nullable Specification<Liked> spec, Pageable pageable);
    List<Liked> findAll(Specification<Liked> spec);
    Optional<Liked> findLikedByUserAndLikedTypeAndTargetId(User user, LikedType likedType, Long targetId);
}
