package com.lettrip.lettripbackend.mongo.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@NoArgsConstructor
@Document(collection = "meet_up_code")
public class MeetUpCode {
    @Id
    private String id;

    @Field(name="meet_up_id")
    private Long meetUpId;
    @Field(name="requester_id")
    private Long requesterId;

    @Field(name="performer_id")
    private Long performerId;

    private String code;
    @Field(name="expiration")
    @Indexed(expireAfterSeconds = 120) // 2분 후에 삭제
    private Date expiration;
    @Builder
    public MeetUpCode(
            Long meetUpId,
            Long requesterId,
            Long performerId,
            String code
    ) {
        this.meetUpId = meetUpId;
        this.requesterId = requesterId;
        this.performerId = performerId;
        this.code = code;
    }
}
