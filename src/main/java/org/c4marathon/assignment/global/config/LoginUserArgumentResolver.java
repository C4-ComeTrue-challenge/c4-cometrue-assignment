package org.c4marathon.assignment.global.config;

import org.c4marathon.assignment.global.annotation.LoginUser;
import org.c4marathon.assignment.user.domain.Users;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

	private final SessionConfig sessionConfig;

	public LoginUserArgumentResolver(SessionConfig sessionConfig) {
		this.sessionConfig = sessionConfig;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Users.class) && parameter.hasParameterAnnotation(LoginUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		return sessionConfig.getSessionUser(request);
	}

}
