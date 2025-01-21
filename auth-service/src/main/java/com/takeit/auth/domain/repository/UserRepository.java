package com.takeit.auth.domain.repository;

import com.querydsl.core.types.Predicate;
import com.takeit.auth.application.dto.UserAuthResponse;
import com.takeit.auth.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUsernameAndIsDeletedFalse(String username);

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    Page<User> findAll(Predicate predicate, Pageable pageable);

    List<User> findAllById(Iterable<Long> userIds);
}
