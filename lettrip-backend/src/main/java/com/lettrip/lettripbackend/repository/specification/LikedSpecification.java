package com.lettrip.lettripbackend.repository.specification;

import com.lettrip.lettripbackend.domain.liked.Liked;
import com.lettrip.lettripbackend.constant.LikedType;
import com.lettrip.lettripbackend.domain.user.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class LikedSpecification {
    public static Specification<Liked> equalUser(User user) {
        return new Specification<Liked>() {
            @Override
            public Predicate toPredicate(Root<Liked> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("user"),user);
            }
        };
    }

    public static Specification<Liked> equalLikedType(LikedType likedType) {
        return new Specification<Liked>() {
            @Override
            public Predicate toPredicate(Root<Liked> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("likedType"),likedType);
            }
        };
    }

    public static Specification<Liked> equalTargetId(Long targetId) {
        return new Specification<Liked>() {
            @Override
            public Predicate toPredicate(Root<Liked> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("targetId"),targetId);
            }
        };
    }

    public static Specification<Liked> getUserLiked(User user, LikedType likedType) {
        return equalUser(user).and(equalLikedType(likedType));
    }


}
