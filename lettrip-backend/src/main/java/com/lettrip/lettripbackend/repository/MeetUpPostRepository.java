package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetUpPostRepository extends JpaRepository<MeetUpPost,Long> {
    Page<MeetUpPost> findAll(@Nullable Specification<MeetUpPost> spec, Pageable pageable);
    Page<MeetUpPost> findAllByUser(User user,Pageable pageable);
}
