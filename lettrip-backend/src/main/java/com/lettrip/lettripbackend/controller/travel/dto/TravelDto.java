package com.lettrip.lettripbackend.controller.travel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lettrip.lettripbackend.domain.travel.Travel;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class TravelDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String title;
        private String travelTheme;
        private boolean isVisited;
        private String province;
        private String city;

        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate departDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate lastDate;
        private long totalCost;
        private int numberOfCourses;

        private List<CourseDto.Request> courses;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        String writerNickname;
        String writerEmail;
        String title;
        String travelTheme;
        String province;
        String city;

        boolean isVisited;

        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate departDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate lastDate;

        long totalCost;
        int numberOfCourses;

        List<CourseDto.Response> courses;

        public Response(Travel travel) {
            this.writerEmail = travel.getUser().getEmail();
            this.writerNickname = travel.getUser().getNickname();
            this.title = travel.getTitle();
            this.travelTheme = travel.getTravelTheme().getKoreanName();
            this.province = travel.getProvince().getKoreanName();
            this.city = travel.getCity();
            this.isVisited = travel.isVisited();
            this.departDate = travel.getDepartDate();
            this.lastDate = travel.getLastDate();
            this.totalCost = travel.getTotalCost();
            this.numberOfCourses = travel.getNumberOfCourses();
            this.courses = travel.getCourses()
                    .stream()
                    .map(CourseDto.Response::new)
                    .collect(Collectors.toList());
        }
    }
}
