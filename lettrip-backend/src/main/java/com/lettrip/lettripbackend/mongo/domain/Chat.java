package com.lettrip.lettripbackend.mongo.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@Document(collection = "chat")
public class Chat {
    @Id
    private String id;
    @Field(name="room_id")
    private String roomId;
    private String message;
    @Field(name="send_user_id")
    private long sendUserId;

    @Field(name="receive_user_id")
    private long receiveUserId;

    @Field(name="created_at")
    private LocalDateTime createdAt;

    @Field(name="is_image")
    private boolean isImage;

    @Builder
    public Chat(String roomId, String message, long sendUserId, long receiveUserId, boolean isImage) {
        this.roomId = roomId;
        this.message = message;
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
        this.createdAt = LocalDateTime.now();
        this.isImage = isImage;
    }
}
