package com.lettrip.lettripbackend.mongo.repository;

import com.lettrip.lettripbackend.mongo.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Page<ChatRoom> findAllByWriteUserIdOrRequestUserId(long writeUserId, long requestUserId, Pageable pageable);
}
