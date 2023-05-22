package com.lettrip.lettripbackend.repository.specification;

import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.constant.TravelTheme;
import com.lettrip.lettripbackend.domain.travel.Travel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TravelSpecification {

    // equal
    public static Specification<Travel> equalProvince(Province province) {
        return new Specification<Travel>() {
            @Override
            public Predicate toPredicate(Root<Travel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("province"),province);
            }
        };
    }

    public static Specification<Travel> equalCity(String city) {
        return new Specification<Travel>() {
            @Override
            public Predicate toPredicate(Root<Travel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("city"), city);
            }
        };
    }

    public static Specification<Travel> equalTravelTheme(TravelTheme travelTheme) {
        return new Specification<Travel>() {
            @Override
            public Predicate toPredicate(Root<Travel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("travelTheme"),travelTheme);
            }
        };
    }
    // between
    public static Specification<Travel> betweenTotalCost(long minCost, long maxCost) {
        return new Specification<Travel>() {
            @Override
            public Predicate toPredicate(Root<Travel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get("totalCost"),minCost,maxCost);
            }
        };
    }

    public static Specification<Travel> betweenNumberOfCourses(int minNumber, int maxNumber) {
        return new Specification<Travel>() {
            @Override
            public Predicate toPredicate(Root<Travel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get("numberOfCourses"),minNumber,maxNumber);
            }
        };
    }
}

