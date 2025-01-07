package com.takeit.favorite.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FavoriteQueryDslConfig {
    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em){

        return new JPAQueryFactory(em);
    }

}
