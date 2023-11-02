package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.meetup.MeetUp;
import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetUpRepository extends JpaRepository<MeetUp,Long> {
    Optional<MeetUp> findByMeetUpPost(MeetUpPost meetUpPost);
}
