package com.takeit.order.application.dto.queue;

public record QueueDto(
        Long rank,
        boolean isActive
) {
    public static QueueDto of(Long rank, boolean isActive) {
        return new QueueDto(rank, isActive);
    }
}
