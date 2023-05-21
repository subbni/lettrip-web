package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.travel.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {

}
