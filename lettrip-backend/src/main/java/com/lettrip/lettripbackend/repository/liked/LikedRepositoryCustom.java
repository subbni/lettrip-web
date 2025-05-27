package com.lettrip.lettripbackend.repository.liked;

import com.lettrip.lettripbackend.domain.community.Article;
import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.domain.travel.Travel;
import com.lettrip.lettripbackend.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LikedRepositoryCustom {
    Page<Travel> findUserLikedTravel(User user, Pageable pageable);
    Page<Article> findUserLikedArticle(User user, Pageable pageable);
    Page<Place> findUserLikedPlace(User user, Pageable pageable);
}
