package com.lettrip.lettripbackend.mongo.repository;

import com.lettrip.lettripbackend.mongo.domain.MeetUpCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetUpCodeRepository extends MongoRepository<MeetUpCode, String> {

    Optional<MeetUpCode> findByMeetUpId(Long meetUpId);
}
