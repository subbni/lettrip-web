package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.liked.dto.LikedDto;
import com.lettrip.lettripbackend.domain.community.Article;
import com.lettrip.lettripbackend.domain.liked.Liked;
import com.lettrip.lettripbackend.constant.LikedType;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.ArticleRepository;
import com.lettrip.lettripbackend.repository.LikedRepository;
import com.lettrip.lettripbackend.repository.specification.LikedSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LikedService {
    private final LikedRepository likedRepository;
    private final ArticleRepository articleRepository;
    private final UserService userService;

    @Transactional
    public ApiResponse saveLiked(Long userId, LikedDto.Request request) {
        User user = userService.findUserById(userId);
        if(!hasLiked(userId,request.getLikedType(), request.getTargetId())) {
            likedRepository.save(
                    Liked.builder()
                            .user(user)
                            .likedType(request.getLikedType())
                            .targetId(request.getTargetId())
                            .build()
            );
            if(request.getLikedType().equals(LikedType.ARTICLE_LIKE)) {
                updateArticle(request.getTargetId(),1);
            }
        }
        return new ApiResponse(true,"좋아요 등록이 완료되었습니다.");
    }

    @Transactional
    public ApiResponse deleteLiked(Long userId, LikedDto.Request request) {
        User user = userService.findUserById(userId);
        Liked liked = likedRepository.findLikedByUserAndLikedTypeAndTargetId(
                user,request.getLikedType(), request.getTargetId()
        ).orElseThrow(()-> {
            throw new ResourceNotFoundException("Liked");
        });

        likedRepository.delete(liked);
        if(request.getLikedType().equals(LikedType.ARTICLE_LIKE)) {
            updateArticle(request.getTargetId(),-1);
        }
        return new ApiResponse(true, "좋아요 삭제가 완료되었습니다.");
    }

    private void updateArticle(Long targetId, int amount) {
        Article article = articleRepository.findById(targetId)
                .orElseThrow(()-> {
                    throw new ResourceNotFoundException("Article","articleId",targetId);
                });

        article.updateLikedCount(amount);
    }

    // 좋아요가 등록되어있는지 확인
    public LikedDto.Response checkLiked(Long userId, LikedType likedType, Long targetId) {
        User user = userService.findUserById(userId);

        Liked liked = likedRepository.findLikedByUserAndLikedTypeAndTargetId(
                    user, likedType, targetId
            ).orElse(null);

        if(liked == null) {
           return new LikedDto.Response(null, false);
        }else {
            return new LikedDto.Response(liked.getId(),true);
        }
    }

    public boolean hasLiked(Long userId, LikedType likedType, Long targetId) {
        User user = userService.findUserById(userId);
        Liked liked = likedRepository.findLikedByUserAndLikedTypeAndTargetId(
                user, likedType, targetId
        ).orElse(null);

        if(liked == null) {
            return false;
        }else {
            return true;
        }
    }

    public List<Liked> findUserLikedList(User user, LikedType likedType) {
        return likedRepository.findAll(
                LikedSpecification.getUserLiked(user, likedType)
        );
    }

    public void deleteLiked(Long userId, LikedType likedType, Long targetId) {
        User user = userService.findUserById(userId);
        likedRepository.findLikedByUserAndLikedTypeAndTargetId(
                user, likedType, targetId
        ).ifPresent(likedRepository::delete);
    }
}
