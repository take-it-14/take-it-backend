package com.takeit.order.application.dto.order;

import java.io.Serializable;
import java.util.UUID;

public record OrderCompleteDto(
	UUID orderId
) implements Serializable {
}
