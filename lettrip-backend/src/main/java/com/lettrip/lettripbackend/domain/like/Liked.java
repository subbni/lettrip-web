package com.lettrip.lettripbackend.domain.like;

import com.lettrip.lettripbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Liked {
    @Id
    @Column(name="LIKED_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private LikedType likedType;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    private long targetId;

    @Builder
    public Liked(LikedType likedType, User user, long targetId) {
        this.likedType = likedType;
        this.user = user;
        this.targetId = targetId;
    }
}
