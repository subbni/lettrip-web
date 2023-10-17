package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.meetup.MeetUp;
import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import com.lettrip.lettripbackend.domain.meetup.Poke;
import com.lettrip.lettripbackend.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokeRepository extends JpaRepository<Poke,Long> {
    Optional<Poke> findByUser(User user);
    Optional<Poke> findByUserAndAndMeetUpPost(User user, MeetUpPost meetUpPost);
    Page<Poke> findAllByUser(User user, Pageable pageable);
    Page<Poke> findAllByMeetUpPost(MeetUpPost meetUpPost, Pageable pageable);
}
