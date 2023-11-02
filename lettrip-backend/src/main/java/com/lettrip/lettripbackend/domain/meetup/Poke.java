package com.lettrip.lettripbackend.domain.meetup;

import com.lettrip.lettripbackend.constant.MeetUpStatus;
import com.lettrip.lettripbackend.constant.PokeStatus;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/*
3. Poke (찌르기)
    - meetUpPost id (unique)
    - user id
    - brief message
    - status
 */
@Getter
@NoArgsConstructor
@Entity
public class Poke {
    @Id
    @Column(name="POKE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="MEET_UP_POST_ID")
    private MeetUpPost meetUpPost;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    private String briefMessage;

    @Enumerated(EnumType.STRING)
    private PokeStatus pokeStatus;

    @Builder
    public Poke(MeetUpPost meetUpPost, User user, String briefMessage) {
        this.meetUpPost = meetUpPost;
        this.user = user;
        this.briefMessage = briefMessage;
        this.pokeStatus = PokeStatus.NOT_YET;
    }
}
