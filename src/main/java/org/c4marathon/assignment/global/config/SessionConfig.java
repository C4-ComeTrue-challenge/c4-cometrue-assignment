package org.c4marathon.assignment.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.c4marathon.assignment.user.domain.Users;
import org.springframework.stereotype.Component;

@Component
public class SessionConfig {

	private static final String LOGIN_USER = "loginUser";

	public void createSession(HttpServletRequest request, Users users) {
		HttpSession session = request.getSession();
		session.setAttribute(LOGIN_USER, users);
	}

	public Users getSessionUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (Users)session.getAttribute(LOGIN_USER);
		}
		return null;
	}

	public void invalidateSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}
}
