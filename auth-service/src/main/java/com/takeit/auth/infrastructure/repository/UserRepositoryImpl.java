package com.takeit.auth.infrastructure.repository;

import com.takeit.auth.domain.entity.User;
import com.takeit.auth.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryImpl extends JpaRepository<User, Long>, UserRepository {

}
