package com.lettrip.lettripbackend.repository.specification;

import com.lettrip.lettripbackend.constant.MissionType;
import com.lettrip.lettripbackend.domain.mission.Mission;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MissionSpecification {
    public static Specification<Mission> equalMissionType(MissionType missionType) {
        return new Specification<Mission>() {
            @Override
            public Predicate toPredicate(Root<Mission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("missionType"),missionType);
            }
        };
    }

    public static Specification<Mission> betweenAccomplishedDate(LocalDate startDate, LocalDate lastDate) {
        return new Specification<Mission>() {
            @Override
            public Predicate toPredicate(Root<Mission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get("accomplishedDate"),startDate,lastDate);
            }
        };
    }

    public static Specification<Mission> getMissionRankers(LocalDate startDate, LocalDate lastDate) {
        return new Specification<Mission>() {
            @Override
            public Predicate toPredicate(Root<Mission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get("accomplishedDate"),startDate,lastDate);
            }
        };
    }

}
