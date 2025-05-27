package com.lettrip.lettripbackend.repository.liked;

import com.lettrip.lettripbackend.constant.LikedType;
import com.lettrip.lettripbackend.domain.liked.Liked;
import com.lettrip.lettripbackend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long>, LikedRepositoryCustom {
    Optional<Liked> findLikedByUserAndLikedTypeAndTargetId(User user, LikedType likedType, Long targetId);
}
