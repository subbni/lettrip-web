package com.lettrip.lettripbackend.controller.comment;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.comment.dto.CommentResponse;
import com.lettrip.lettripbackend.controller.comment.dto.CreateComment;
import com.lettrip.lettripbackend.controller.comment.dto.ModifyComment;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * /api/comment/{article-id}	GET	댓글 리스트 조회
 * /api/comment/create	POST	댓글 작성
 * /api/comment/modify	PUT	댓글 수정
 * /api/comment/delete/{comment-id}	DELETE	댓글 삭제
 */

@RequiredArgsConstructor
@RequestMapping("api/comment")
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public CreateComment.Response createComment(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody CreateComment.Request request
            ) {
        return commentService.saveComment(request, customUserDetails.getId());
    }

    @PutMapping("/modify")
    public ModifyComment.Response modifyComment(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody ModifyComment.Request request
    ) {
        return commentService.modifyComment(request, customUserDetails.getId());
    }

    @GetMapping("/{article-id}")
    public Page<CommentResponse> showCommentList(
            @PathVariable("article-id") Long articleId,
            Pageable pageable
    ) {
        return commentService.showCommentsPage(articleId,pageable);
    }

    @DeleteMapping("/delete/{commentId}")
    public ApiResponse deleteComment(
            @CurrentUser CustomUserDetails customUserDetails,
            @PathVariable("commentId") Long commentId
    ) {
        return commentService.deleteComment(commentId, customUserDetails.getId());
    }

}
