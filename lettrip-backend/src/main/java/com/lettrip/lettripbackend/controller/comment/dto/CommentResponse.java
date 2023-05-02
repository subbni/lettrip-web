package com.lettrip.lettripbackend.controller.comment.dto;

import com.lettrip.lettripbackend.domain.community.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private Long user_id;
    private Long parent_comment_id;
    private Long mentioned_user_id;
    private String nickname;
    private LocalDateTime createdDate;
    private String content;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.user_id = comment.getUser().getId();
        this.parent_comment_id = comment.getParent_comment_id();
        this.mentioned_user_id = comment.getMentioned_user_id();
        this.nickname = comment.getUser().getNickname();
        this.createdDate = comment.getCreatedDate();
        this.content = comment.getContent();
    }
}
