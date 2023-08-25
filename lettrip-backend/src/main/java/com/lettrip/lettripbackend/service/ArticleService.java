package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.ArticleType;
import com.lettrip.lettripbackend.constant.LikedType;
import com.lettrip.lettripbackend.controller.article.dto.CreateArticle;
import com.lettrip.lettripbackend.controller.article.dto.ModifyArticle;
import com.lettrip.lettripbackend.controller.article.dto.ShowArticle;
import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.article.dto.ShowArticleList;
import com.lettrip.lettripbackend.domain.community.Article;
import com.lettrip.lettripbackend.domain.liked.Liked;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.ArticleRepository;
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
public class ArticleService {
    private final UserService userService;
    private final LikedService likedService;
    private final ArticleRepository articleRepository;

    @Transactional
    public CreateArticle.Response saveArticle(CreateArticle.Request request) {
        User user = userService.findUserByEmail(request.getEmail());
        return CreateArticle.Response.fromEntity(
                articleRepository.save(
                        Article.builder()
                                .user(user)
                                .title(request.getTitle())
                                .content(request.getContent())
                                .articleType(ArticleType.valueOf(request.getArticleType()))
                                .build()
                )
        );
    }

    @Transactional
    public ShowArticle.Response showArticle(long articleId) {
        return ShowArticle.Response.fromEntity(
          updateArticleHit(findArticleById(articleId))
        );
    }

    @Transactional
    public ModifyArticle.Response updateArticle(
            ModifyArticle.Request request,
            Long userId
    ) {
        Article article = findArticleById(request.getId());
        checkIfWriter(article,userId);
        return ModifyArticle.Response.fromEntity(
                article.update(request.getTitle(),request.getContent())
        );
    }

    @Transactional
    public ApiResponse deleteArticle(Long articleId, Long userId) {
        Article article = findArticleById(articleId);
        checkIfWriter(article, userId);
        articleRepository.delete(article);
        return new ApiResponse(true,"해당 게시글이 삭제되었습니다.");
    }

    // 게시글 전체 조회
    public Page<ShowArticleList.Response> getAllArticlePage(Pageable pageable) {
        Page<Article> page = articleRepository.findAll(pageable);
        return new PageImpl<ShowArticleList.Response>(
                articleToDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    // 사용자 작성 게시글 조회
    public Page<ShowArticleList.Response> getUserArticlePage(Long userId, Pageable pageable) {
        User user = userService.findUserById(userId);
        Page<Article> page = articleRepository.findByUser(user, pageable);
        return new PageImpl<ShowArticleList.Response>(
                articleToDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    // 좋아요 누른 게시글 조회
    public Page<ShowArticleList.Response> getLikedPlaces(Long userId, Pageable pageable) {
        User user = userService.findUserById(userId);
        List<Liked> likedList = likedService.findUserLikedList(user, LikedType.ARTICLE_LIKE);
        List<Article> likedArticleList = likedList.stream()
                .map((liked)-> {
                    return articleRepository.findById(liked.getTargetId())
                            .orElse(null);
                }).toList();
        return new PageImpl<>(
                articleToDto(likedArticleList),
                pageable,
                likedArticleList.size()
        );
    }

    private List<ShowArticleList.Response> articleToDto(List<Article> articleList) {
        return articleList.stream()
                .map(ShowArticleList.Response::fromEntity)
                .collect(Collectors.toList());
    }
    private void checkIfWriter(Article article, Long userId) {
        if(article.getUser().getId()!=userId) {
            throw new SecurityException("작성자만 가능한 작업입니다."); // TODO: Exception 따로 만들기
        }
    }

    @Transactional
    protected Article updateArticleHit(Article article) {
        article.addHit();
        return article;
    }

    @Transactional
    public void updateArticleCommentCount(Article article, int amount) {
        article.updateCommentCount(amount);
    }

    @Transactional
    public void updateLikedCount(Long articleId, int amount) {
        Article article = findArticleById(articleId);
        article.updateLikedCount(amount);
    }

    public Article findArticleById(long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(()-> {
                    throw new ResourceNotFoundException("Article","articleId",articleId);
                });
    }



}
