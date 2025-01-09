package com.takeit.order.application.interceptor;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.takeit.order.application.dto.user.UserDto;
import com.takeit.order.infrastructure.client.UserClient;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserCheckInterceptor implements HandlerInterceptor {
	private final UserClient userClient;

	public UserCheckInterceptor(@Lazy UserClient userClient) {
		this.userClient = userClient;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String username = request.getHeader("X-Username");
		String role = request.getHeader("X-Role");

		UserDto userDto = userClient.getUser(username);
		if (!role.equals(userDto.role())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
			return false;
		}

		response.addHeader("X-UserId", userDto.id().toString());

		return true;
	}
}
