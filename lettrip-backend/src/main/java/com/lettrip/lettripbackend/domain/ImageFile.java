package com.lettrip.lettripbackend.domain;

import com.lettrip.lettripbackend.domain.travel.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IMAGE_FILE_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="REVIEW_ID")
    private Review review;

    @Column(nullable = false)
    private String originalFileName; // 파일 원본명

    @Column(nullable = false)
    private String storedFileName; // 저장된 파일명

    @Column(nullable = false)
    private String storedFileUrl; // 접근 가능한 파일 url

    private long fileSize;

    @Builder
    public ImageFile(
            String originalFileName,
            String storedFileName,
            String storedFileUrl,
            long fileSize
    ) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.storedFileUrl = storedFileUrl;
        this.fileSize = fileSize;
    }

    public void setReview(Review review) {
        this.review = review;
        if(!review.getImageFiles().contains(this)) {
            review.getImageFiles().add(this);
        }
    }

}
