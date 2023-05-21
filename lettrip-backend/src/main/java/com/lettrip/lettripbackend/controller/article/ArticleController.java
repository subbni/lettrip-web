package com.lettrip.lettripbackend.controller.article;

import com.lettrip.lettripbackend.controller.article.dto.CreateArticle;
import com.lettrip.lettripbackend.controller.article.dto.ModifyArticle;
import com.lettrip.lettripbackend.controller.article.dto.ShowArticle;
import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.article.dto.ShowArticleList;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/create")
    public CreateArticle.Response createArticle(
            @Valid @RequestBody CreateArticle.Request request
    ) {
        return articleService.saveArticle(request);
    }

    @PutMapping("/modify")
    public ModifyArticle.Response modifyArticle(
            @Valid @RequestBody ModifyArticle.Request request,
            @CurrentUser CustomUserDetails customUserDetails
            ) {
        // 해당 게시글의 작성자만 게시글을 수정할 수 있어야 한다.
        return articleService.updateArticle(
                request, customUserDetails.getId()
        );
    }

    @DeleteMapping("/delete/{articleId}")
    public ApiResponse deleteArticle(
            @PathVariable("articleId") Long articleId,
            @CurrentUser CustomUserDetails customUserDetails
    ) {
        return articleService.deleteArticle(
                articleId, customUserDetails.getId()
        );
    }

    @GetMapping("/{articleId}")
    public ShowArticle.Response showArticle(@PathVariable("articleId") Long articleId) {
        return articleService.showArticle(articleId);
    }

    @GetMapping
    public Page<ShowArticleList.Response> showArticleListPage(Pageable pageable) {
        return articleService.showAllArticlePage(pageable);
    }


}
