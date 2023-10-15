package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetUpPostRepository extends JpaRepository<MeetUpPost,Long> {

}
