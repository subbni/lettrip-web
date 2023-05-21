package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.travel.dto.CourseDto;
import com.lettrip.lettripbackend.domain.travel.Course;
import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.domain.travel.Review;
import com.lettrip.lettripbackend.domain.travel.Travel;
import com.lettrip.lettripbackend.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final PlaceService placeService;
    private final ReviewService reviewService;
    @Transactional
    public void saveCourses(Travel travel, List<CourseDto.Request> courseDtoList) {
        for(CourseDto.Request courseDto : courseDtoList) {
            saveCourse(travel,courseDto);
        }
    }

    @Transactional
    public void saveCourse(Travel travel, CourseDto.Request courseDto) {
        Place place = placeService.savePlace(courseDto.getPlace());
        Review review = reviewService.saveReview(travel.getUser(),place,courseDto.getReview());
        courseRepository.save(
                Course.builder()
                        .travel(travel)
                        .arrivedTime(courseDto.getArrivedTime())
                        .cost(courseDto.getCost())
                        .dayCount(courseDto.getDayCount())
                        .place(place)
                        .review(review)
                        .build()
        );
    }

}
