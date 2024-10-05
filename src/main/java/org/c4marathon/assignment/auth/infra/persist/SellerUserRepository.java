package org.c4marathon.assignment.auth.infra.persist;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.UUID;

import org.c4marathon.assignment.auth.domain.User;
import org.c4marathon.assignment.auth.domain.UserRepository;
import org.c4marathon.assignment.auth.domain.vo.UserType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SellerUserRepository implements UserRepository {
	private final JdbcTemplate jdbcTemplate;

	public SellerUserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		String sql = "select id, email, password from sellers where email = ?";
		User user = jdbcTemplate.queryForObject(sql, userRowMapper(), email);
		return Optional.ofNullable(user);
	}

	@Override
	public boolean supports(UserType type) {
		return type == UserType.SELLER;
	}

	private RowMapper<User> userRowMapper() {
		return (rs, rowNum) -> {
			byte[] bytesId = rs.getBytes("id");
			ByteBuffer bf = ByteBuffer.wrap(bytesId);
			long high = bf.getLong();
			long low = bf.getLong();
			UUID id = new UUID(high, low);
			return new User(id, rs.getString("email"), rs.getString("password"), UserType.SELLER);
		};
	}
}
