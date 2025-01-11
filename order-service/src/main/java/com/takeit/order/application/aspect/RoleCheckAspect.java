package com.takeit.order.application.aspect;

import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.order.application.annotation.RequireRole;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RoleCheckAspect {

	private final HttpServletRequest request;

	@Before("@annotation(requireRole)")
	public void checkUser(RequireRole requireRole) {
		String role = request.getHeader("X-Role");

		List<String> allowedRoles = List.of(requireRole.value());
		if(!allowedRoles.contains(role)) throw new CustomException(ErrorCode.FORBIDDEN);
	}
}
