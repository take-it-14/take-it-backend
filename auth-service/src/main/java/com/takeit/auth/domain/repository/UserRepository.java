package com.takeit.auth.domain.repository;

import com.takeit.auth.domain.entity.User;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUsernameAndIsDeletedFalse(String username);

    Optional<User> findByEmailAndIsDeletedFalse(String email);

}
