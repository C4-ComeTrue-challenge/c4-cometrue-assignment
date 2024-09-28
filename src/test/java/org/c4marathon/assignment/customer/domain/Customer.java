package org.c4marathon.assignment.customer.domain;

import java.util.UUID;

/**
 * <h1>구매자/판매자 별 가입시 필요정보</h1>
 *
 * <table>
 *     <tr>
 *         <th></th>
 *         <th>구매자</th>
 *         <th>판매자</th>
 *     </tr>
 *     <tr>
 *         <td>공통</td>
 *         <td colspan="2">이메일, 비밀번호, 전화번호, 이름</td>
 *     </tr>
 *     <tr>
 *         <td>추가 정보</td>
 *         <td>N/A</td>
 *         <td>
 *             <p>대표자 명, 상호, 사업장 주소, 통신판매업신고번호, 입점 담당자 명, 정산계좌</p>
 *         </td>
 *     </tr>
 * </table>
 *
 * <br/>
 *
 * <h1>계정 인증 방안</h1>
 *
 * <h3>1. 판매자/구매자 계정을 통합하는 방안</h3>
 *
 * <p>
 *     사용자는 하나의 계정으로 구매 및 판매를 할 수 있다.
 * </p>
 * <p>
 *     우선 계정을 통합관리하기 위해서는 부모테이블이 필요하다.
 *     id나 이메일과 같이 중복을 방지해야하는 컬럼들이 있기 때문이다.
 *     회원이 보유하고 있는 권한(판매 권한/구매 권한)을 저장하고, 그 권한에 따라 필요한 추가 정보들도 저장해야 한다.
 * </p>
 * <p>
 *     이 때 단일 User 도메인 객체가 모든 유형의 로직(충전/구매/판매 등)을 담당하게 만들 수도 있다.
 *     하지만 이 경우 (로그인과 같은) 소수의 공통 기능을 제외한 대부분의 기능에 권한을 체크하는 로직이 추가되어야 한다.
 *     따라서 구매자/판매자의 도메인을 분리하는 것이 좋을 것 같다.
 *     그러면 구체적으로 어떤 구조로 분리할 수 있을까?
 * </p>
 *
 * <h5>상속 vs 합성</h5>
 *
 * <p>
 *     상속은 공통 로직을 부모 클래스(User 객체)에 구현함으로써 중복 코드를 줄이는 데에 도움이 된다.
 *     판매자(또는 구매자) 객체는 이를 상속받아 자식 클래스가 된다.
 *     합성은 단일 User 객체가 판매자(또는 구매자) 객체를 속성으로 가지면서 필요한 경우 이 객체들과 협력을 통해 도메인 문제를 해결하게 된다.
 *     즉, 상속은 판매자(또는 구매자) 객체가, 합성은 User 객체가 에그리거트가 된다.
 * </p>
 * <p>
 *     후자의 경우 엄밀하게 말하면 도메인이 분리되었다고 할 수 없지만, 판매자/구매자를 구분짓지 않고 User로 통합하여 기능을 구현할 수 있기 때문에 서비스 코드의 복잡도를 줄일 수 있겠다.
 *     (로그인과 같이) 판매자/구매자 구분없는 기능(또는 이중적인 지위를 가지는 기능)이 많으면 이 방법이 더 좋겠다.
 *     하지만 커머스에서는 대부분의 기능이 판매자/구매자가 구분되어 있으며 각각은 그에 따른 역할을 수행하기 때문에 이들을 User로 통합하게 되면 실수할 여지가 생기고, 또한 도메인 코드가 복잡해질 수 있다.
 *     (예를 들어, 구매자가 구매자로부터 상품을 구매할 수도 있지 않을까?)
 * </p>
 * <p>
 *     전자의 경우 인증 과정에서 필요한 데이터를 부모 클래스(User 객체)가 가지고 있고, 판매자(또는 구매자)는 이를 상속받는 구조가 된다.
 *     그런데 이런 상속관계를 RDB에 영속화하기 위해서는 SINGLE_TABLE/TABLE_PER_CLASS/JOINED 중 택1 해야 한다.
 * </p>
 * <table>
 *     <tr>
 *         <th>유형</th>
 *         <th>장점</th>
 *         <th>단점</th>
 *     </tr>
 *     <tr>
 *         <td>SINGLE_TABLE</td>
 *         <td>N/A</td>
 *         <td><p>모든 컬럼에 null을 허용해야 한다.</p></td>
 *     </tr>
 *     <tr>
 *         <td>JOINED</td>
 *         <td>N/A</td>
 *         <td><p>Insert: 두 번의 Insert 쿼리 발생</p><p>Select: Join 쿼리 발생</p></td>
 *     </tr>
 *     <tr>
 *         <td>TABLE_PER_CLASS</td>
 *         <td>N/A</td>
 *         <td><p>Select: UNION 쿼리 발생</p></td>
 *     </tr>
 * </table>
 * <p>
 *     어느 하나도 마음에 드는 게 없다. 극소수의 공통 로직 처리를 위해 감수할 만한 트레이드 오프는 아닌 것 같다.
 *     회원은 다른 테이블과 연관관계를 가지기 때문에 NoSQL을 사용하기에도 무리가 있는 것 같다.
 * </p>
 *
 * <h3>2. 판매자/구매자 계정을 분리하는 방안</h3>
 *
 * <p>
 *     사용자는 역할에 따른 계정을 각각 생성해야 한다.(즉, 구매시에는 구매자로, 판매시에는 판매자로 로그인해야한다.)
 *     위의 방안과 차이점이 있다면 이제는 부모 테이블이 필요없기 때문에 "판매자/구매자의 테이블을 완전히 분리할 수 있다"는 것이다.
 * </p>
 *
 * <h5>문제점: 인증과정에서 공통 로직</h5>
 *
 * <p>
 *     판매자/구매자의 인증 프로세스는 정책에 따라 추가적인 절차(ex.2FA)가 필요할 수 있지만 근본적으로는 동일하다.
 *     따라서 공통 로직 처리를 위해 "인터페이스"를 상속받는 것이 좋겠다.
 *     계정을 통합했을 때는 "부모 클래스"가 필요했는데 이제는 부모테이블이 없기 때문에 "인터페이스"로 처리할 수 있다.
 * </p>
 * <p>
 *     대신에 판매자/구매자는 다른 endpoint를 통해 인증을 해야한다.
 *     그렇지 않으면 인증하기 위해 판매자,구매자 테이블을 모두 조회해야하기 때문이다.
 * </p>
 *
 * <h3>결론. 판매자/구매자 계정 분리 + 상속(인터페이스)</h3>
 *
 */
public class Customer {
	private UUID id;
	private String email;
	private String password;
	private String name;

	public Customer(UUID id, String email, String encodedPassword, String name) {
		this.id = id;
		this.email = email;
		this.password = encodedPassword;
		this.name = name;
	}

	public Customer(String email, String password, String name, PasswordEncoder passwordEncoder) {
		this.email = email;
		this.password = passwordEncoder.encode(password);
		this.name = name;
	}

	public UUID getId() {
		return id;
	}
}
