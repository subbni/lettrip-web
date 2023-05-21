package com.lettrip.lettripbackend.domain.travel;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Course {
    @Id
    @Column(name="COURSE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="TRAVEL_ID")
    private Travel travel;

    @ManyToOne
    @JoinColumn(name="PLACE_ID")
    private Place place;

    @OneToOne
    @JoinColumn(name="REVIEW_ID", referencedColumnName = "REVIEW_ID")
    private Review review;
    private long cost;

    @Column(name="ARRIVED_TIME")
    LocalDateTime arrivedTime;

    @Min(1) @Max(7)
    private int dayCount;


    @Builder
    public Course(
            Travel travel,
            Place place,
            Review review,
            long cost,
            LocalDateTime arrivedTime,
            int dayCount
    ) {
        this.travel = travel;
        this.place = place;
        this.cost = cost;
        this.review = review;
        this.arrivedTime = arrivedTime;
        this.dayCount = dayCount;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }
}
