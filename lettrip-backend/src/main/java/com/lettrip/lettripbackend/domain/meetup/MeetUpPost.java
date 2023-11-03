package com.lettrip.lettripbackend.domain.meetup;

import com.lettrip.lettripbackend.constant.MeetUpPostStatus;
import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.constant.converter.ProvincePersistConverter;
import com.lettrip.lettripbackend.domain.BaseTimeEntity;
import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.domain.travel.Travel;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class MeetUpPost extends BaseTimeEntity {
    @Id
    @Column(name="MEET_UP_POST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    @OneToOne
    @JoinColumn(name="MEET_UP_ID")
    private MeetUp meetUp;

    @Column(name="is_gps_enabled")
    private boolean isGpsEnabled;
    private LocalDateTime meetUpDate;

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
                      boolean isGpsEnabled,
                      LocalDateTime meetUpDate,
                      Province province,
                      String city,
                      String title,
                      String content,
                      Place place,
                      Travel travel
                      ) {
        this.user = user;
        this.isGpsEnabled = isGpsEnabled;
        this.meetUpDate = meetUpDate;
        this.province = province;
        this.city = city;
        this.title = title;
        this.content = content;
        this.place = place;
        this.travel = travel;
        this.meetUpPostStatus = MeetUpPostStatus.UNSCHEDULED;
    }

    public MeetUpPost update(boolean isGpsEnabled,
                  LocalDateTime meetUpDate,
                  String title,
                  String content,
                  Place place,
                  Travel travel) {
        this.isGpsEnabled = isGpsEnabled;
        this.meetUpDate = meetUpDate;
        this.title = title;
        this.content = content;
        this.place = place;
        this.travel = travel;
        return this;
    }

    public MeetUpPost setMeetUp(MeetUp meetUp) {
        this.meetUp = meetUp;
        return this;
    }

    public MeetUpPost setMeetUpPostStatus(MeetUpPostStatus meetUpPostStatus) {
        this.meetUpPostStatus = meetUpPostStatus;
        return this;
    }
}
