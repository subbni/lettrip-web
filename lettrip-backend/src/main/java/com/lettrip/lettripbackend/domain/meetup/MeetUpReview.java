package com.lettrip.lettripbackend.domain.meetup;

import com.lettrip.lettripbackend.constant.MeetUpStatus;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/*
- id (unique)
- MeetUp id
- Writer user id
- Object user id
- content
 */
@Getter
@NoArgsConstructor
@Entity
public class MeetUpReview {
    @Id
    @Column(name="MEET_UP_REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name="MEET_UP_ID")
    private MeetUp meetUp;

    @Enumerated(EnumType.STRING)
    private MeetUpStatus meetUpStatus;

    @ManyToOne
    @JoinColumn(name="WRITE_USER_ID")
    private User writeUser;

    @ManyToOne
    @JoinColumn(name="OBJECT_USER_ID")
    private User objectUser;

    String content;

    @Builder
    public MeetUpReview(MeetUp meetUp, MeetUpStatus meetUpStatus, User writeUser, User objectUser, String content) {
        this.meetUp = meetUp;
        this.meetUpStatus = meetUpStatus;
        this.writeUser = writeUser;
        this.objectUser = objectUser;
        this.content = content;
    }

}
