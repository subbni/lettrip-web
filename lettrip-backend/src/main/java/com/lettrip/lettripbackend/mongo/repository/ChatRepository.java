package com.lettrip.lettripbackend.mongo.repository;

import com.lettrip.lettripbackend.mongo.domain.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends MongoRepository<Chat,String> {
    Page<Chat> findAllByRoomId(String RoomId, Pageable pageable);
    Chat findFirstBySendUserId(long sendUserId);
}
