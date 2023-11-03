package com.lettrip.lettripbackend.repository.specification;

import com.lettrip.lettripbackend.constant.MeetUpPostStatus;
import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class MeetUpPostSpecification {
    public static Specification<MeetUpPost> getAll() {
        return new Specification<MeetUpPost>() {
            @Override
            public Predicate toPredicate(Root<MeetUpPost> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.isNotNull(root.get("isGpsEnabled"));
            }
        };
    }
    public static Specification<MeetUpPost> equalProvince(Province province) {
        return new Specification<MeetUpPost>() {
            @Override
            public Predicate toPredicate(Root<MeetUpPost> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("province"),province);
            }
        };
    }

    public static Specification<MeetUpPost> equalCity(String city) {
        return new Specification<MeetUpPost>() {
            @Override
            public Predicate toPredicate(Root<MeetUpPost> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("city"),city);
            }
        };
    }

    public static Specification<MeetUpPost> equalMeetUpPostStatus(MeetUpPostStatus meetUpPostStatus) {
        return new Specification<MeetUpPost>() {
            @Override
            public Predicate toPredicate(Root<MeetUpPost> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("meetUpPostStatus"),meetUpPostStatus);
            }
        };
    }


    public static Specification<MeetUpPost> equalIsGpsEnabled(Boolean isGpsEnabled) {
        return new Specification<MeetUpPost>() {
            @Override
            public Predicate toPredicate(Root<MeetUpPost> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("isGpsEnabled"),isGpsEnabled);
            }
        };
    }
}
