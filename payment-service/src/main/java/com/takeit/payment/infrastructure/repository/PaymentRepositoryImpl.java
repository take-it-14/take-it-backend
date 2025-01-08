package com.takeit.payment.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.takeit.payment.domain.entity.Payment;
import com.takeit.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.takeit.payment.domain.entity.QPayment.payment;

@RequiredArgsConstructor
@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Payment save(Payment payment) {
        return jpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> getPaymentByOrderId(Long orderId) {
        JPAQuery<Payment> query = queryFactory
                .selectFrom(payment)
                .where(payment.orderId.eq(orderId).and(payment.isDeleted.eq(false)));

        return Optional.ofNullable(query.fetchOne());
    }
}
