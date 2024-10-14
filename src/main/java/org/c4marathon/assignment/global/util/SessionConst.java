package org.c4marathon.assignment.global.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionConst {
    public static final String LOGIN_USER = "loginUser";

    public static Long getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_USER) == null) {
            return null;
        }

        return (Long) session.getAttribute(SessionConst.LOGIN_USER);
    }
}
