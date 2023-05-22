package com.lettrip.lettripbackend.controller.travel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lettrip.lettripbackend.domain.travel.Travel;
import lombok.*;

import java.time.LocalDate;

public class ShowTravelList {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        String province;
        String city;

        Long minCost;

        Long maxCost;

        Integer minNumberOfCourses;

        Integer maxNumberOfCourses;

        String travelTheme;
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

        public static ShowTravelList.Response fromEntity(Travel travel) {
            return Response.builder()
                    .writerEmail(travel.getUser().getEmail())
                    .writerNickname(travel.getUser().getNickname())
                    .title(travel.getTitle())
                    .travelTheme(travel.getTravelTheme().toString())
                    .province(travel.getProvince().toString())
                    .city(travel.getCity())
                    .isVisited(travel.isVisited())
                    .departDate(travel.getDepartDate())
                    .lastDate(travel.getLastDate())
                    .numberOfCourses(travel.getNumberOfCourses())
                    .build();
        }
    }
}