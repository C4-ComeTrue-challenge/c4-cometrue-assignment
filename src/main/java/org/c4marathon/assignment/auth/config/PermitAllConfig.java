package org.c4marathon.assignment.auth.config;

import java.util.ArrayList;
import java.util.List;

public class PermitAllConfig {

    private static final List<String> permitAllMatchers = new ArrayList<>();

    public static String[] getPermitAllMatchers() {
        return permitAllMatchers.toArray(new String[0]);
    }

    static {
        permitAllMatchers.add("/members");
        permitAllMatchers.add("/members/*");
        permitAllMatchers.add("/actuator");
        permitAllMatchers.add("/actuator/*");
        permitAllMatchers.add("/test");
    }
}
