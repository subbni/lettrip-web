package com.lettrip.lettripbackend.mongo.domain;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class ChatRoom {
    @Id
    private String id;
    @Field(name="meet_up_post_id")
    private String meetUpPostId;
    @Field(name="meet_up_id")
    private String meetUpId;
    @Field(name="write_user_id")
    private String writeUserId;
    @Field(name="request_user_id")
    private String requestUserId;
    @Field(name="last_message")
    private String lastMessage;
    @Field(name="meet_up_status")
    private String meetUpStatus;
}
