package org.c4marathon.assignment.auth.domain;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.c4marathon.assignment.auth.domain.vo.UserType;
import org.c4marathon.assignment.common.authentication.model.principal.Principal;
import org.c4marathon.assignment.common.encoder.PasswordEncoder;

public class User {
	private UUID id;
	private String email;
	private String encodedPassword;
	private List<String> roles;
	private UserType userType;

	public User(UUID id, String email, String encodedPassword, UserType userType) {
		this.id = id;
		this.email = email;
		this.encodedPassword = encodedPassword;
		this.roles = List.of("ROLE_" + userType.name());
		this.userType = userType;
	}

	public boolean matches(String password, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(password, encodedPassword);
	}

	public Principal getPrincipal() {
		return userType.getPrincipal(id);
	}

	public Collection<String> getAuthorities() {
		return roles;
	}
}
