package com.lettrip.lettripbackend.mongo.domain;

import com.lettrip.lettripbackend.constant.MeetUpStatus;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Document(collection = "chat_room")
public class ChatRoom {
    @Id
    private String id;
    @Field(name="meet_up_post_id")
    private long meetUpPostId;
    @Field(name="meet_up_id")
    private long meetUpId;
    @Field(name="write_user_id")
    private long writeUserId;
    @Field(name="request_user_id")
    private long requestUserId;
    @Field(name="last_message")
    private String lastMessage;

    @Field(name="last_message_time")
    private LocalDateTime lastMessageTime;
    @Field(name="meet_up_status")
    @Enumerated(EnumType.STRING)
    private MeetUpStatus meetUpStatus;

    @Builder
    public ChatRoom(
            long meetUpPostId, long meetUpId, long writeUserId,
            long requestUserId, String lastMessage, MeetUpStatus meetUpStatus
    ) {
        this.meetUpPostId = meetUpPostId;
        this.meetUpId = meetUpId;
        this.writeUserId = writeUserId;
        this.requestUserId = requestUserId;
        this.lastMessage = lastMessage;
        this.meetUpStatus = meetUpStatus;

    }

    public void updateLastMessageAndTime(String lastMessage, LocalDateTime lastMessageTime) {
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }
}
