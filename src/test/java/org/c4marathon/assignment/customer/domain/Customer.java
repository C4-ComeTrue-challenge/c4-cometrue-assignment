package org.c4marathon.assignment.customer.domain;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * <h1>쟁점1. Domain 엔티티와 Persistence 엔티티의 분리</h1>
 *
 * <h3>왜?</h3>
 *
 * <h5>책임 분리</h5>
 *
 * <p>
 *     Domain 엔티티는 순수한 JAVA 코드로 작성해서 외부에 대한 의존성을 제거한다.
 *     Domain 엔티티는 도메인에 변경사항이 발생하지 않는 이상 수정할 일이 없다.
 *     즉, 도메인에 관련된 로직만 담당하기 때문에 변경 전파(Change Propagation)를 방지할 수 있다.
 * </p>
 *
 * <h5>가독성</h5>
 *
 * <p>
 *     Column에 대한 세부적인 설정을 할 예정인데, 도메인 로직과 JPA 관련 어노테이션이 엉켜있으면 가독성이 떨어진다.
 * </p>
 *
 * <h5>인프라에 대한 도메인의 의존성 제거</h5>
 *
 * <p>
 *     Domain과 Persistence 엔티티가 통합되어 있으면 JPA에서 정해진 규칙에 따라 데이터를 조회하게 된다.
 *     즉, Domain 엔티티가 table schema(또는 DBMS)에 대해 의존하게 된다.
 *     반면에 분리되어 있으면 의존성을 제거할 수 있다.
 * </p>
 *
 * <h5>테스트하기 쉬운 코드?</h5>
 *
 * <p>
 *     Domain 엔티티는 검증없이 생성되어서는 안된다.
 *     도메인 생성시 PasswordEncoder같은 값을 암호화하는 기능이 요구될 수도 있다.
 *     따라서 생성자 호출시 파라미터로 넘겨받는 클래스(인터페이스)와 협력하여(또는 static 메서드 호출을 통해) 엔티티를 생성을 하게 된다.
 * </p>
 * <p>
 *     하지만 이렇게 만들어진 생성자는 "테스트를 위한 데이터를 생성"할 때 곤란하다.
 *     클래스(인터페이스)를 mocking 해야하고, 이 마저도 곤란한 경우가 발생할 수 있다.
 * </p>
 * <p>
 *     그런데 Domain과 Persistence 엔티티를 서로 매핑하기 위해서는 어차피 전체 생성자(또는 필드나 setter)가 public 접근제어자로 열려있어야한다.
 *     그렇다면 mocking없이 데이터를 생성할 수 있을 것이다.
 *     테스트 가능성은 Domain 엔티티와 Persistence 엔티티의 분리하는 이유 중 한가지로 꼽히는데 아직은 잘 모르겠다.
 * </p>
 *
 * <h3>오버엔지니어링 아닌가</h3>
 *
 * <p>
 *     초기 서비스는 Domain 엔티티와 Persistence 엔티티를 분리할 필요성이 떨어진다.
 *     서비스가 확장되면서 table schema(또는 DBMS)의 변경으로 인해 이를 분리할 필요성이 생기면 처리하는 것이 좋겠다.
 * </p>
 *
 * <h3>결론. 통합</h3>
 *
 * <br/>
 *
 * <h1>쟁점2. 계정 인증 방안</h1>
 *
 * <h3>구매자/판매자 별 가입시 필요정보</h1>
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
 * <h3>1) 판매자/구매자 계정을 통합하는 방안</h3>
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
 * <h3>2) 판매자/구매자 계정을 분리하는 방안</h3>
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
 * <h3>결론. 판매자/구매자 계정 분리(테이블,클래스,endpoint 모두 분리) + 상속(인터페이스)</h3>
 *
 * <br/>
 *
 * <h1>Id 생성 전략</h1>
 *
 * <h3>1. auto_increment</h3>
 *
 * <p>회원은 예측가능한 id를 피하고 싶다.</p>
 *
 * <h3>2. UUID</h3>
 *
 * <p>유니크하고 랜덤한 값으로 Id를 만들자.</p>
 *
 * <h5>1) v1: time-based version</h5>
 *
 * <p>
 *     PK는 클러스터 인덱스가 된다(MySQL 기준).
 *     따라서 Id가 시간순으로 생성되면 INSERT 수행시 1)데이터 및 클러스터 인덱스 페이지를 수정할 필요가 없고, 2)인덱스 페이지에 대한 cache_hit가 높아져서 Disk I/O가 덜 발생하여 성능상의 이점이 있다.
 *     하지만 동시성을 고려하면서 Id를 생성하기 위해서는 bottle-neck이 발생한다.
 *     여러 스레드가 동시에 UUID를 생성할 때 중복을 방지하기 위해 잠금(lock)이나 CAS(Compare And Swap) 같은 동시성 제어 메커니즘이 필요하기 때문이다.
 * </p>
 *
 * <h5>2) v4: randomly or pseudo-randomly generated version</h5>
 *
 * <p>
 *     Id가 랜덤하게 생성되기 때문에 INSERT 수행시 1)데이터 및 클러스터 인덱스 페이지에 수정이 필요할 수 있고, 2)인덱스 페이지에 대한 cache_hit가 낮아져서 Disk I/O가 상대적으로 빈번하게 발생한다.
 *     하지만 (클러스터 인덱스의 변경이 필요없는) SELECT/UPDATE 수행시 위와 비교해서 차이가 거의 없다.
 *     (cursor-based pagination이 곤란한 점은 있겠다)
 * </p>
 *
 * <h3>결론. UUID v1</h3>
 *
 * <p>
 *     우선 동시성 제어로 인한 Id 생성의 bottle-neck이 어느정도인지 확인이 필요하다.
 *     다음은 로컬에서 테스트한 결과이다.
 * </p>
 * <table>
 *     <tr>
 *         <th></th>
 *         <th>auto_increment</th>
 *         <th>uuid v1</th>
 *         <th>uuid v4</th>
 *     </tr>
 *     <tr>
 *         <th>소요 시간(ms)</th>
 *         <td>109</td>
 *         <td>114</td>
 *         <td>125</td>
 *     </tr>
 * </table>
 * <p>
 *     로컬 테스트 결과는 별 차이없는 것 같다.
 *     UUID v4에서 v1으로 변경하는 것은 리소스가 많이 들기 때문에 우선 v1을 채택하기로 결정했다.
 *     추후 테스트를 통해 v4로 변경할 수도 있겠다.
 * </p>
 *
 * @see org.hibernate.annotations.UuidGenerator.Style
 * @see org.hibernate.id.uuid.CustomVersionOneStrategy
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4122">RFC 4122 - A Universally Unique IDentifier (UUID) URN Namespace</a>
 * @see <a href="https://dev.to/mcadariu/using-uuids-as-primary-keys-3e7a">To UUID, or not to UUID, that is the primary key question</a>
 *
 */
@Entity
@Table(
	name = "CUSTOMERS",
	uniqueConstraints = {@UniqueConstraint(name = "uq_customers_email", columnNames = {"email"})}
)
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	private UUID id;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String name;

	protected Customer() {
	}

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
