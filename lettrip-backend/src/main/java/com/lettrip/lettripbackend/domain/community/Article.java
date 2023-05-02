package com.lettrip.lettripbackend.domain.community;

import com.lettrip.lettripbackend.domain.BaseTimeEntity;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Article extends BaseTimeEntity {
    @Id
    @Column(name="ARTICLE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    private String title;

    private String content;

    private int hit;

    private int likeCount;

    @Builder
    public Article(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.hit = 0;
        this.likeCount = 0;
    }

    public Article update(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }
    public void updateLikeCount(int amount) {
        likeCount += amount;
    }

    public void addHit() {
        hit++;
    }

}
