package com.lettrip.lettripbackend.repository.meetUpPost;

import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetUpPostRepositoryCustom {
    Page<MeetUpPost> findAll(BooleanBuilder condition, Pageable pageable);
}
