package com.takeit.auth.domain.repository;

import com.querydsl.core.types.Predicate;
import com.takeit.auth.domain.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUsernameAndIsDeletedFalse(String username);

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    Page<User> findAll(Predicate predicate, Pageable pageable);
}
