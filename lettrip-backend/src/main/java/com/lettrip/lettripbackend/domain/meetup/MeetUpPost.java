package com.lettrip.lettripbackend.domain.meetup;

import com.lettrip.lettripbackend.constant.MeetUpPostStatus;
import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.constant.MeetUpStatus;
import com.lettrip.lettripbackend.constant.converter.ProvincePersistConverter;
import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.domain.travel.Travel;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/*
- id (unique)
- user id
- isGPSEnabled
- title
- meetUpDate
- province
- city
- content
- place (nullable)
- travel id (nullable) (여행 계획 게시글)
- status
 */
@Getter
@NoArgsConstructor
@Entity
public class MeetUpPost {
    @Id
    @Column(name="MEET_UP_POST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    @Column(name="is_gps_enbaled")
    private boolean isGPSEnabled;
    private LocalDate meetUpDate;

    @Convert(converter = ProvincePersistConverter.class)
    private Province province;

    private String city;
    private String title;
    private String content;

    @Nullable
    @ManyToOne
    @JoinColumn(name="PLACE_ID")
    private Place place;

    @Nullable
    @ManyToOne
    @JoinColumn(name="TRAVEL_ID")
    private Travel travel;

    @Enumerated(EnumType.STRING)
    private MeetUpPostStatus meetUpPostStatus;
    @Builder
    public MeetUpPost(User user,
                      boolean isGPSEnabled,
                      LocalDate meetUpDate,
                      Province province,
                      String city,
                      String title,
                      String content
                      ) {
        this.user = user;
        this.isGPSEnabled = isGPSEnabled;
        this.meetUpDate = meetUpDate;
        this.province = province;
        this.city = city;
        this.title = title;
        this.content = content;
        this.meetUpPostStatus = MeetUpPostStatus.UNSCHEDULED;
    }
}
