package org.c4marathon.assignment.customer.ui;

import org.c4marathon.assignment.common.api.ApiResponse;
import org.c4marathon.assignment.customer.ui.dto.request.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <h5>화면</h5>
 * <p>
 *     1) 회원가입 및 로그인 페이지를 분리하거나 2) 페이지 내에서 사용자가 어떤 역할의 계정에 회원가입 또는 로그인할 것인지 매번 체크해주어야 한다.
 * </p>
 * <p>
 *     혼동과 불편함을 피하기 위해서 화면 자체를 분리하는 것이 좋겠다.
 *     CSRF 토큰을 제공하기 위해서 SSR(Server Side Rendering)을 한다는 가정하에, 로그인 페이지는 공통? 상관없다. type만 다르게 주면 되니까.
 * </p>
 *
 * <h5>서버</h5>
 * <p>
 *     1) endpoint를 분리할지 2) 공유하되, 파라미터로 type(판매자/구매자)을 받을지 결정해야 한다.
 * </p>
 *
 * @see org.c4marathon.assignment.customer.domain.Customer
 */
public interface CustomerApi {
	@PostMapping("/customers")
	ResponseEntity<ApiResponse<Void>> postSignUp(@RequestBody SignUpRequest request);
}
