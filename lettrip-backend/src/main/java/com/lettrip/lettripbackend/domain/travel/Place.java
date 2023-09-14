package com.lettrip.lettripbackend.domain.travel;

import com.lettrip.lettripbackend.constant.PlaceCategory;
import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.constant.converter.ProvincePersistConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;


import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Place {
    @Id
    @Column(name="PLACE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="PLACE_NAME")
    private String name;

    @Column(nullable = false, columnDefinition = "GEOMETRY")
    private Point locationPoint;
    @Enumerated(value=EnumType.STRING)
    private PlaceCategory categoryCode;
    private String categoryName;

    @Convert(converter = ProvincePersistConverter.class)
    Province province;
    String city;

    private String address;

    @OneToMany(mappedBy = "place")
    private List<Review> reviews = new ArrayList<>();

    private float totalRating;

    @Max(1)
    private float soloFriendlyTotalRating;

    @Builder
    public Place(
            String name,
            Point locationPoint,
            PlaceCategory categoryCode,
            String categoryName,
            Province province,
            String city,
            String address
    ) {
        this.name = name;
        this.locationPoint = locationPoint;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.province = province;
        this.city = city;
        this.address = address;
        this.totalRating = 0f;
        this.soloFriendlyTotalRating = 0f;
    }
    public void addReviews(Review review) {
        this.reviews.add(review);
    }

    public void updateTotalRating(int newRating) {
        if(totalRating==0f) {
            totalRating = newRating;
        }
        totalRating = (totalRating*reviews.size()+ newRating)/(reviews.size()+1);
    }

    public void updateSoloFriendlyTotalRating(int newRating) {
        if(soloFriendlyTotalRating ==0f) {
            soloFriendlyTotalRating = newRating;
        }
        soloFriendlyTotalRating = (soloFriendlyTotalRating *reviews.size()+ newRating)/(reviews.size()+1);
    }
}
