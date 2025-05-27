package com.lettrip.lettripbackend.repository.meetUpPost;

import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import com.lettrip.lettripbackend.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface MeetUpPostRepository extends JpaRepository<MeetUpPost, Long>, MeetUpPostRepositoryCustom {
    Page<MeetUpPost> findAllByUser(User user, Pageable pageable);
}
