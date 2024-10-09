package org.c4marathon.assignment.auth.application;

import java.util.Collection;
import java.util.List;

import org.c4marathon.assignment.auth.domain.CompositeUserRepository;
import org.c4marathon.assignment.auth.domain.User;
import org.c4marathon.assignment.auth.domain.vo.UserType;
import org.c4marathon.assignment.common.authentication.model.Authentication;
import org.c4marathon.assignment.common.authentication.model.principal.AnonymousPrincipal;
import org.c4marathon.assignment.common.authentication.model.principal.Principal;
import org.c4marathon.assignment.common.encoder.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SignInService {
	private final CompositeUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SignInService(CompositeUserRepository compositeUserRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = compositeUserRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Authentication signIn(Command cmd) {
		User findUser = userRepository.findByTypeAndEmail(cmd.userType(), cmd.email()).orElseThrow();
		boolean matches = findUser.matches(cmd.password(), passwordEncoder);
		if (matches) {
			return new Authentication() {
				@Override
				public Principal getPrincipal() {
					return findUser.getPrincipal();
				}

				@Override
				public Collection<String> getAuthorities() {
					return findUser.getAuthorities();
				}

				@Override
				public boolean isAuthenticated() {
					return true;
				}
			};
		}
		return new Authentication() {
			@Override
			public Principal getPrincipal() {
				return new AnonymousPrincipal();
			}

			@Override
			public Collection<String> getAuthorities() {
				return List.of();
			}

			@Override
			public boolean isAuthenticated() {
				return false;
			}
		};
	}

	public record Command(
		String email,
		String password,
		UserType userType
	) {
	}
}
