package com.lettrip.lettripbackend.mongo.repository;

import com.lettrip.lettripbackend.mongo.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends MongoRepository<Chat,String> {
}
