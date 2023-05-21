package com.lettrip.lettripbackend.domain.travel;

import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.constant.TravelTheme;
import com.lettrip.lettripbackend.constant.converter.ProvincePersistConverter;
import com.lettrip.lettripbackend.constant.converter.TravelThemeConverter;
import com.lettrip.lettripbackend.domain.BaseTimeEntity;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Travel extends BaseTimeEntity {

    @Id
    @Column(name = "TRAVEL_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @NotNull
    String title;

    @Convert(converter = ProvincePersistConverter.class)
    Province province;
    String city;
    private boolean isVisited;

    private LocalDate departDate;

    private LocalDate lastDate;

    private long totalCost;

    @Max(24)
    private int numberOfCourses;

    @Convert(converter = TravelThemeConverter.class)
    private TravelTheme travelTheme;

    @OneToMany(
            mappedBy = "travel",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Course> courses = new ArrayList<>();

    @Builder
    public Travel(
            User user,
            String title,
            Province province,
            String city,
            boolean isVisited,
            LocalDate departDate,
            LocalDate lastDate,
            long totalCost,
            int numberOfCourses,
            TravelTheme travelTheme
    ) {
        this.user = user;
        this.title = title;
        this.province = province;
        this.city = city;
        this.isVisited = isVisited;
        this.departDate = departDate;
        this.lastDate = lastDate;
        this.totalCost = totalCost;
        this.numberOfCourses = numberOfCourses;
        this.travelTheme = travelTheme;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
        if (course.getTravel() != this) {
            course.setTravel(this);
        }
    }
}
