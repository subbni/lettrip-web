package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.comment.dto.CommentResponse;
import com.lettrip.lettripbackend.controller.comment.dto.CreateComment;
import com.lettrip.lettripbackend.controller.comment.dto.ModifyComment;
import com.lettrip.lettripbackend.controller.comment.dto.ShowComments;
import com.lettrip.lettripbackend.domain.community.Article;
import com.lettrip.lettripbackend.domain.community.Comment;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CommentService {
    private final ArticleService articleService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Transactional
    public CreateComment.Response saveComment(CreateComment.Request request, Long userId) {

        User user = userService.findUserById(userId);
        User mentionedUser = userService.findUserByEmail(request.getMentioned_user_email());
        Article article = articleService.findArticleById(request.getArticle_id());
        Comment parent = findCommentById(request.getParent_comment_id());
        return CreateComment.Response.fromEntity(
                commentRepository.save(
                        Comment.builder()
                                .user(user)
                                .article(article)
                                .content(request.getContent())
                                .parent_comment_id(parent.getId())
                                .mentioned_user_id(mentionedUser.getId())
                                .build()
                )
        );
    }

    @Transactional
    public ModifyComment.Response modifyComment(ModifyComment.Request request, Long userId) {
        Comment comment = findCommentById(request.getId());
        checkIfWriter(comment, userId);
        comment.update(request.getContent());
        return ModifyComment.Response.fromEntity(comment);
    }

    public Page<CommentResponse> showCommentsPage(Long articleId, Pageable pageable) {
        Article article = articleService.findArticleById(articleId);
        Page<Comment> page = commentRepository.findByArticle(article, pageable);
        return new PageImpl<CommentResponse>(commentToDto(page.getContent()),
                pageable, page.getTotalElements());
    }

    public ApiResponse deleteComment(Long commentId, Long userId) {
        // 1. 본인 작성 댓글인지 확인
        Comment comment = findCommentById(commentId);
        checkIfWriter(comment,userId);
        // 2. 삭제
        return new ApiResponse(true,"댓글이 삭제되었습니다.");
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(()-> {
                    throw new ResourceNotFoundException("Comment","commentId",commentId);
                });
    }

    private void checkIfWriter(Comment comment, Long userId) {
        if(comment.getUser().getId()!=userId) {
            throw new SecurityException("작성자만 가능한 작업입니다."); // TODO: Exception 따로 만들기
        }
    }

    public List<CommentResponse> commentToDto(List<Comment> commentList) {
        return commentList.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }
}