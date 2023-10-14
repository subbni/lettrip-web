package com.lettrip.lettripbackend.mongo.domain;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Chat {
    @Id
    private String id;
    private String roomId;
    private String message;
    @Field(name="send_user_id")
    private String sendUserId;

    @Field(name="receive_user_id")
    private String receiveUserId;

    @Field(name="created_at")
    private String createAt;

    @Field(name="is_image")
    private boolean isImage;
}
