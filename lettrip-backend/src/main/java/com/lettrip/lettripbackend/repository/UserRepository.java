package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
