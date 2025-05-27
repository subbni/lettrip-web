package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.comment.dto.CommentResponse;
import com.lettrip.lettripbackend.controller.comment.dto.CreateComment;
import com.lettrip.lettripbackend.controller.comment.dto.ModifyComment;
import com.lettrip.lettripbackend.domain.community.Article;
import com.lettrip.lettripbackend.domain.community.Comment;
import com.lettrip.lettripbackend.domain.community.QComment;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.comment.CommentRepository;
import com.querydsl.core.BooleanBuilder;
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
        Article article = articleService.findArticleById(request.getArticle_id());
        articleService.updateArticleCommentCount(article,1);

        if(request.getParent_comment_id() == null && request.getMentioned_user_email()==null) {
            return saveParentComment(user,article,request);
        } else {
            return saveReplyComment(user,article,request);
        }
    }

    private CreateComment.Response saveParentComment(
            User user,
            Article article,
            CreateComment.Request request) {
        return CreateComment.Response.fromEntity(
                commentRepository.save(
                        Comment.builder()
                                .user(user)
                                .article(article)
                                .content(request.getContent())
                                .parent_comment_id(-1L)
                                .mentioned_user_id(-1L)
                                .build()
                )
        );
    }

    private CreateComment.Response saveReplyComment(
            User user,
            Article article,
            CreateComment.Request request) {
        User mentionedUser = userService.findUserByEmail(request.getMentioned_user_email());
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

    public Page<CommentResponse> showParentCommentPage(Long articleId, Pageable pageable) {
        Article article = articleService.findArticleById(articleId);
//        Page<Comment> page = commentRepository.findAll(getParentCommentPageSpec(article),pageable);
        Page<Comment> page = commentRepository.findAll(buildParentCommentCondition(article), pageable);
        return new PageImpl<CommentResponse>(commentToDto(page.getContent()),
                pageable, page.getTotalElements());
    }

    public Page<CommentResponse> showReplyCommentPage(Long articleId, Long parentCommentId, Pageable pageable) {
        Article article = articleService.findArticleById(articleId);
//        Page<Comment> page = commentRepository.findAll(getReplyCommentPageSpec(article, parentCommentId),pageable);
        Page<Comment> page = commentRepository.findAll(buildReplyCommentCondition(article, parentCommentId), pageable);
        return new PageImpl<CommentResponse>(commentToDto(page.getContent()),
                pageable, page.getTotalElements());
    }

    @Transactional
    public ApiResponse deleteComment(Long commentId, Long userId) {
        // 1. 본인 작성 댓글인지 확인
        Comment comment = findCommentById(commentId);
        Article article = comment.getArticle();

        checkIfWriter(comment,userId);
        // 2. 삭제
        commentRepository.delete(comment);
        articleService.updateArticleCommentCount(article,-1);
        return new ApiResponse(true,"댓글이 삭제되었습니다.");
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(()-> {
                    throw new ResourceNotFoundException("Comment","commentId",commentId);
                });
    }

    private BooleanBuilder buildParentCommentCondition(Article article) {
        QComment qComment = QComment.comment;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(qComment.article.eq(article));
        condition.and(qComment.parent_comment_id.lt(0));
        return condition;
    }

    private BooleanBuilder buildReplyCommentCondition(Article article, Long parentCommentId) {
        QComment qComment = QComment.comment;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(qComment.article.eq(article));
        condition.and(qComment.parent_comment_id.eq(parentCommentId));
        return condition;
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
