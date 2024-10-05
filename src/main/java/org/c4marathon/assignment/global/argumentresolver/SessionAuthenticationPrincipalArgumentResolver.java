package org.c4marathon.assignment.global.argumentresolver;

import java.lang.annotation.Annotation;

import org.c4marathon.assignment.common.authentication.annotation.AuthenticationPrincipal;
import org.c4marathon.assignment.global.session.SessionConst;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionAuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return findMethodAnnotation(AuthenticationPrincipal.class, parameter) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		boolean errorOnInvalidType = findMethodAnnotation(AuthenticationPrincipal.class, parameter)
			.errorOnInvalidType();
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		HttpSession session = request.getSession(false);
		if (session == null) {
			if (errorOnInvalidType) {
				throw new ClassCastException(null + " is not assignable to " + parameter.getParameterType());
			}
			return null;
		}
		Object principal = session.getAttribute(SessionConst.SESSION_AUTHENTICATION_PRINCIPAL_KEY);
		if (principal != null && !ClassUtils.isAssignable(parameter.getParameterType(), principal.getClass())) {
			if (errorOnInvalidType) {
				throw new ClassCastException(principal + " is not assignable to " + parameter.getParameterType());
			}
			return null;
		}
		return principal;
	}

	private <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass, MethodParameter parameter) {
		T annotation = parameter.getParameterAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}
		Annotation[] annotationsToSearch = parameter.getParameterAnnotations();
		for (Annotation toSearch : annotationsToSearch) {
			annotation = AnnotationUtils.findAnnotation(toSearch.annotationType(), annotationClass);
			if (annotation != null) {
				return annotation;
			}
		}
		return null;
	}
}
