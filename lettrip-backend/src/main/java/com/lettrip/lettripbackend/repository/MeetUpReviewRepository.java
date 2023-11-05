package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.constant.MeetUpStatus;
import com.lettrip.lettripbackend.domain.meetup.MeetUp;
import com.lettrip.lettripbackend.domain.meetup.MeetUpReview;
import com.lettrip.lettripbackend.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetUpReviewRepository extends JpaRepository<MeetUpReview,Long> {
    Optional<MeetUpReview> findByMeetUpAndWriteUser(MeetUp meetUp, User user);
    Page<MeetUpReview> findAllByObjectUserAndMeetUpStatus(User user, MeetUpStatus meetUpStatus, Pageable pageable);

}
