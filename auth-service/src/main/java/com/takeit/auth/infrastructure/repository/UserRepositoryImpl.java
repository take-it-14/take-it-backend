package com.takeit.auth.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.takeit.auth.domain.entity.QUser;
import com.takeit.auth.domain.entity.User;
import com.takeit.auth.domain.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface UserRepositoryImpl extends JpaRepository<User, Long>, UserRepository,
        QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {

    @Override
    default void customize(QuerydslBindings querydslBindings, @NotNull QUser qUser) {
        // 모든 String 필드에 대해 동적 검색
        querydslBindings.bind(String.class).all((StringPath path, Collection<? extends String> values) -> {
            List<String> valueList = new ArrayList<>(values.stream().map(String::trim).toList());
            if (valueList.isEmpty()) {
                return Optional.empty();
            }
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            //booleanBuilder.and(qUser.isDeleted.eq(false)); // is_deleted = false

            for (String s : valueList) {
                booleanBuilder.or(path.containsIgnoreCase(s));
            }
            return Optional.of(booleanBuilder);
        });

        // 민감한 필드는 검색 불가능하도록 설정
        querydslBindings.excluding(qUser.password);

    }

}
