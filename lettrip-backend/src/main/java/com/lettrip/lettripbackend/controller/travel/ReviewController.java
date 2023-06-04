package com.lettrip.lettripbackend.controller.travel;

import com.lettrip.lettripbackend.controller.travel.dto.ReviewDto;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{placeId}")
    public Page<ReviewDto.Response> showReviewPageByPlace(
            @PathVariable("placeId") Long placeId,
            Pageable pageable
    ) {
        return reviewService.getAllReviewPageByPlace(placeId,pageable);
    }
}
