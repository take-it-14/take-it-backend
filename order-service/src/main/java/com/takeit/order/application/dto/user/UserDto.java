package com.takeit.order.application.dto.user;

public record UserDto(
	Long id,
	String username,
	String nickname,
	String email,
	String role
) {
}
