package com.lettrip.lettripbackend.repository.specification;

import com.lettrip.lettripbackend.domain.community.Article;
import com.lettrip.lettripbackend.domain.community.Comment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class CommentSpecification {
    public static Specification<Comment> equalArticle(Article article){
        return new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("article"),article);
            }
        };
    }

    public static Specification<Comment> noParentCommentId() {
        return new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.lessThan(root.get("parent_comment_id"),0);
            }
        };
    }

    public static Specification<Comment> equalParentCommentId(Long id) {
        return new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("parent_comment_id"),id);
            }
        };
    }
}
