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
        int visitTimes;
        List<String> fileUrls;

        String detailReview;

        int rating;

        int soloFriendlyRating;

        public Response(Review review) {
            this.visitTimes = review.getVisitTimes();
            this.fileUrls = FileService.getImageFileUrls(review.getImageFiles());
            this.detailReview = review.getDetailedReview();
            this.rating = review.getRating();
            this.soloFriendlyRating = review.getSoloFriendlyRating();
        }
    }

}
