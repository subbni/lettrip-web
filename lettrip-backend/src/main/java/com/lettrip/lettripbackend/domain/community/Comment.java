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
    @Column(name="MENTIONED_USER_ID")
    private Long mentioned_user_id;
    @NotNull
    @NotBlank
    private String content;

    @Column(name="PARENT_COMMENT_ID")
    private Long parent_comment_id;

    @Builder
    public Comment(
            User user,
            Article article,
            String content,
            Long parent_comment_id,
            Long mentioned_user_id
            ) {
        this.user = user;
        this.article = article;
        this.content = content;
        this.parent_comment_id = parent_comment_id;
        this.mentioned_user_id = mentioned_user_id;

    }

    public void update(String content) {
        this.content = content;
    }
}
