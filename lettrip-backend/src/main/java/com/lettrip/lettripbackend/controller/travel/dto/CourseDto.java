package com.lettrip.lettripbackend.controller.travel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lettrip.lettripbackend.domain.travel.Course;
import jakarta.annotation.Nullable;
import lombok.*;

import java.time.LocalDateTime;


public class CourseDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime arrivedTime;

        private long cost;

        private int dayCount;

        private PlaceDto.Request place;
        private ReviewDto.Request review;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime arrivedTime;

        private long cost;

        private int dayCount;

        private PlaceDto.Response place;

        @Nullable
        private ReviewDto.Response review;

        public Response(Course course) {
            this.arrivedTime = course.getArrivedTime();
            this.cost = course.getCost();
            this.dayCount = course.getDayCount();
            this.place = new PlaceDto.Response(course.getPlace());
            this.review = new ReviewDto.Response(course.getReview());
        }

    }
}
