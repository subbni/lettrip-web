package com.lettrip.lettripbackend.controller.travel.dto;

import com.lettrip.lettripbackend.constant.PlaceCategory;
import com.lettrip.lettripbackend.domain.travel.Place;
import jakarta.annotation.Nullable;
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
        @Nullable
        private String categoryCode;
        @Nullable
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
        PlaceCategory categoryCode;
        String categoryName;
        String province;
        String city;

        public Response(Place place) {
            this.name = place.getName();
            this.categoryCode = place.getCategoryCode();
            this.categoryName = place.getCategoryName();
            this.xpoint = place.getLocationPoint().getX();
            this.ypoint  = place.getLocationPoint().getY();
            this.province = place.getProvince().getKoreanName();
            this.city = place.getCity();
        }
    }

}
