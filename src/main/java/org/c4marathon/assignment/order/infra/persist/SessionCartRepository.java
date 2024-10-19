package org.c4marathon.assignment.order.infra.persist;

import java.util.Optional;
import java.util.UUID;

import org.c4marathon.assignment.global.session.SessionConst;
import org.c4marathon.assignment.order.domain.Cart;
import org.c4marathon.assignment.order.domain.CartRepository;
import org.springframework.stereotype.Repository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Repository
public class SessionCartRepository implements CartRepository {
	private final HttpServletRequest request;

	public SessionCartRepository(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public Optional<Cart> findByCustomerId(UUID customerId) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return Optional.empty();
		}
		Cart cart = (Cart)session.getAttribute(SessionConst.CART_KEY);
		return Optional.ofNullable(cart);
	}

	@Override
	public Cart save(Cart cart) {
		HttpSession session = request.getSession(true);
		session.setAttribute(SessionConst.CART_KEY, cart);
		return cart;
	}
}
