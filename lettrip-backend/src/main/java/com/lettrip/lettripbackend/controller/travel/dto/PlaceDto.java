package com.lettrip.lettripbackend.controller.travel.dto;

import com.lettrip.lettripbackend.domain.travel.Place;
import jakarta.validation.constraints.NotNull;
import lombok.*;


public class PlaceDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private String name;
        @NotNull
        private double xpoint;
        @NotNull
        private double ypoint;
        @NotNull
        private String categoryCode;
        @NotNull
        private String categoryName;
        @NotNull
        String province;
        @NotNull
        String city;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        String name;
        double xpoint;
        double ypoint;

        String province;
        String city;

        public Response(Place place) {
            this.name = place.getName();
            this.xpoint = place.getLocationPoint().getX();
            this.ypoint  = place.getLocationPoint().getY();
            this.province = place.getProvince().getKoreanName();
            this.city = place.getCity();
        }
    }

}
