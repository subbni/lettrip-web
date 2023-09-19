package com.lettrip.lettripbackend.domain.travel;

import com.lettrip.lettripbackend.constant.PlaceCategory;
import com.lettrip.lettripbackend.domain.BaseTimeEntity;
import com.lettrip.lettripbackend.domain.ImageFile;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @Column(name="REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name="PLACE_ID")
    private Place place;

    private int visitTimes;

    private String placeName;

    private String placeCategory;
    // TODO: REVIEW 테이블 컬럼명과의 불일치, placeCategoryName이나 categoryName으로의 변경
    private String address;

    @OneToMany(
            mappedBy = "review",
            cascade = {CascadeType.ALL, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<ImageFile> imageFiles = new ArrayList<>();

    private String detailedReview;

    // 별점
    @Max(5)
    @Min(1)
    @NotNull
    private int rating;

    @Max(1)
    @Min(0)
    private int soloFriendlyRating;

    @Builder
    public Review(
            User user,
            Place place,
            int visitTimes,
            String detailedReview,
            int rating,
            int soloFriendlyRating
    ) {
        this.user=user;
        this.place=place;
        this.visitTimes=visitTimes;
        this.detailedReview=detailedReview;
        this.rating=rating;
        this.soloFriendlyRating = soloFriendlyRating;
        this.placeName = place.getName();
        this.placeCategory = place.getCategoryName();
        this.address = place.getAddress();
    }

    public void update(String detailedReview) {
        this.detailedReview=detailedReview;
    }

//    public void setImageFiles(List<ImageFile> imageFiles) {
//        this.imageFiles = imageFiles;
//    }
//    public void addImageFile(ImageFile imageFile) {
//        this.imageFiles.add(imageFile);
//        if(imageFile.getReview()!=this) {
//            imageFile.setReview(this);
//        }
//    }
}
