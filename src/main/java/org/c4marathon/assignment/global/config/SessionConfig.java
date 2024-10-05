package org.c4marathon.assignment.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.c4marathon.assignment.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class SessionConfig {

    private static final String LOGIN_USER = "loginUser";

    public void createSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_USER, user);
    }

    public User getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute(LOGIN_USER);
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
