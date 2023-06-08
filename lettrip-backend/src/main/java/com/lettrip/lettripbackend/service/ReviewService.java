package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.PlaceCategory;
import com.lettrip.lettripbackend.controller.travel.dto.ReviewDto;
import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.domain.travel.Review;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final FileService fileService;
    private final PlaceService placeService;
    private final ReviewRepository reviewRepository;
    private final HashSet<PlaceCategory> soloFriendlyRatingCategories
            = new HashSet<>(Arrays.asList(PlaceCategory.CE7, PlaceCategory.FD6));

    @Transactional
    public Review saveReview(User user, Place place, ReviewDto.Request reviewDto) {
        // 해당 place의 총평점 업데이트 <- 반드시 review 저장 이전에 해줘야 함
        place.updateTotalRating(reviewDto.getRating());
        checkAndUpdateSoloFriendlyTotalRating(place,reviewDto.getSoloFriendlyRating());
        Review review = reviewRepository.save(
                Review.builder()
                        .place(place)
                        .user(user)
                        .visitTimes(getVisitTimes(user,place)+1)
                        .detailedReview(reviewDto.getDetailedReview())
                        .rating(reviewDto.getRating())
                        .soloFriendlyRating(reviewDto.getSoloFriendlyRating())
                        .build()
        );
        fileService.uploadImageFiles(review, reviewDto.getFileNames());
        return review;
    }

    @Transactional
    public void deleteReview(Review review) {
        // 해당 place 총평점 업데이트
    }

    public Page<ReviewDto.Response> getAllReviewPageByPlace(Long placeId, Pageable pageable) {
        Place place = placeService.findById(placeId);
        Page<Review> page = reviewRepository.findByPlace(place, pageable);
        return new PageImpl<ReviewDto.Response>(
                reviewToDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    @Transactional
    public void checkAndUpdateSoloFriendlyTotalRating(Place place, int newRating) {
//        if(soloFriendlyRatingCategories.contains(place.getCategoryCode())) {
//            place.updateSoloFriendlyTotalRating(newRating);
//        }
        place.updateSoloFriendlyTotalRating(newRating);
    }
    private int getVisitTimes(User user, Place place) {
        return reviewRepository.findByUserAndPlace(user,place).size();
    }

    private List<ReviewDto.Response> reviewToDto(List<Review> reviewList) {
        return reviewList.stream()
                .map(ReviewDto.Response::fromEntity)
                .collect(Collectors.toList());
    }


}
