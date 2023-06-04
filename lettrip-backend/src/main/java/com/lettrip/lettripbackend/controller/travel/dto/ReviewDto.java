package com.lettrip.lettripbackend.controller.travel.dto;

import com.lettrip.lettripbackend.domain.travel.Review;
import com.lettrip.lettripbackend.service.FileService;
import jakarta.annotation.Nullable;
import lombok.*;

import java.util.List;


public class ReviewDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @Nullable
        private List<String> fileNames;
        private String detailedReview;
        private int rating;
        private int soloFriendlyRating;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        long id;
        int visitTimes;
        List<String> fileUrls;

        String detailReview;

        int rating;

        int soloFriendlyRating;

        public Response(Review review) {
            this.id = review.getId();
            this.visitTimes = review.getVisitTimes();
            this.fileUrls = FileService.getImageFileUrls(review.getImageFiles());
            this.detailReview = review.getDetailedReview();
            this.rating = review.getRating();
            this.soloFriendlyRating = review.getSoloFriendlyRating();
        }

        public static ReviewDto.Response fromEntity(Review review) {
            return Response.builder()
                    .id(review.getId())
                    .visitTimes(review.getVisitTimes())
                    .fileUrls(FileService.getImageFileUrls(review.getImageFiles()))
                    .detailReview(review.getDetailedReview())
                    .rating(review.getRating())
                    .soloFriendlyRating(review.getSoloFriendlyRating())
                    .build();
        }
    }

}
