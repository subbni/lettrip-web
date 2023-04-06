package com.lettrip.lettripbackend.domain.community;

import com.lettrip.lettripbackend.domain.BaseTimeEntity;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@Entity
public class Comment extends BaseTimeEntity {
    @Id
    @Column(name="COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name="ARTICLE_ID")
    private Article article;

    @NotNull
    @NotBlank
    private String content;

    @Builder
    public Comment(User user, Article article, String content) {
        this.user = user;
        this.article = article;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
