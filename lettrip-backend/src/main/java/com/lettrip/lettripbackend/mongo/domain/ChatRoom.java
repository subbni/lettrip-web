package com.lettrip.lettripbackend.mongo.domain;

import com.lettrip.lettripbackend.constant.MeetUpStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Long meetUpPostId;
    @Field(name="meet_up_id")
    private Long meetUpId;
    @Field(name="write_user_id")
    private Long writeUserId;
    @Field(name="request_user_id")
    private Long requestUserId;
    @Field(name="last_message")
    private String lastMessage;

    @Field(name="last_message_time")
    private LocalDateTime lastMessageTime;
    @Field(name="meet_up_status")
    @Enumerated(EnumType.STRING)
    private MeetUpStatus meetUpStatus;

    @Builder
    public ChatRoom(
            Long meetUpPostId, Long meetUpId, Long writeUserId,
            Long requestUserId, String lastMessage, MeetUpStatus meetUpStatus
    ) {
        this.meetUpPostId = meetUpPostId;
        this.writeUserId = writeUserId;
        this.requestUserId = requestUserId;
        this.lastMessage = lastMessage;
        this.meetUpId = meetUpId;
        this.meetUpStatus = meetUpStatus;

    }

    public void updateLastMessageAndTime(String lastMessage, LocalDateTime lastMessageTime) {
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public void setMeetUpId(Long meetUpId) {
        this.meetUpId = meetUpId;
    }
    public void setMeetUpStatus(MeetUpStatus meetUpStatus) { this.meetUpStatus = meetUpStatus;}
}
