package com.lettrip.lettripbackend.domain.meetup;

import com.lettrip.lettripbackend.constant.MeetUpStatus;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/*
- id (unique)
- meetUpPost id
- writer (user) id
- requester (user) id
- isDateModified (bool)
- meetUpDate
- status
 */
@Getter
@NoArgsConstructor
@Entity
public class MeetUp {
    @Id
    @Column(name="MEET_UP_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = {CascadeType.ALL, CascadeType.REMOVE})
    @JoinColumn(name="MEET_UP_POST_ID")
    private MeetUpPost meetUpPost;

    @ManyToOne
    @JoinColumn(name="WRITE_USER_ID")
    private User writeUser;
    @ManyToOne
    @JoinColumn(name="REQUEST_USER_ID")
    private User requestUser;
    private LocalDate meetUpDate;
    @Enumerated(EnumType.STRING)
    private MeetUpStatus meetUpStatus;

    @Builder
    public MeetUp( User writeUser, User requestUser,MeetUpPost meetUpPost, LocalDate meetUpDate) {
        this.writeUser = writeUser;
        this.requestUser = requestUser;
        this.meetUpPost= meetUpPost;
        this.meetUpDate = meetUpDate;
        this.meetUpStatus = MeetUpStatus.PENDING;
    }

    public MeetUp setMeetUpStatus(MeetUpStatus meetUpStatus) {
        this.meetUpStatus = meetUpStatus;
        return this;
    }
}
