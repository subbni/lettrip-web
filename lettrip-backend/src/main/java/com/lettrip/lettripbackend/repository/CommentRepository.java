package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.community.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
