# FlashPay

## 개요
**FlashPay**는 순간적으로 수천 건의 결제 요청이 몰리는 한정 수량 이벤트(예: 콘서트 티켓, 한정판 굿즈 등)에서 안정적으로 트래픽을 처리하고, 빠르고 정확한 정산을 가능하게 하는 결제 처리 시스템입니다.

---

## 사용 기술 스택

| 영역        | 기술 및 도구                             |
|-------------|-----------------------------------------|
| Language    | Java 17, Gradle                         |
| Framework   | Spring Boot, Spring MVC, Spring WebFlux, Spring Security |
| 인증        | JWT (JSON Web Token)                    |
| DB          | MySQL, Redis                            |
| Messaging   | Apache Kafka                            |
| 인프라      | Docker, Spring Cloud Config         |
| 문서화      | Swagger (OpenAPI 3.0)                   |
| 테스트      | JMeter (성능 테스트)                     |

---

## 폴더 구조

```bash
FlashPay/
├── src/
│   ├── main/
│   │   ├── java/com/flashpay/
│   │   │   ├── config/         # 보안, Kafka, Redis 등 설정
│   │   │   ├── controller/     # REST API 컨트롤러
│   │   │   ├── domain/         # Entity 클래스
│   │   │   ├── dto/            # 요청/응답 DTO
│   │   │   ├── service/        # 비즈니스 로직
│   │   │   ├── repository/     # JPA 인터페이스
│   │   │   └── util/           # JWT 등 유틸성 클래스
│   │   └── resources/
│   │       ├── application.yml
│   │       └── static/
└── build.gradle
```

---

## ERD 설계 (초안)

```plaintext
[User]
- id (PK)
- username (unique)
- email (unique)
- password
- createdAt

[Product]
- id (PK)
- title
- price
- stock
- startTime
- createdAt

[Order]
- id (PK)
- user_id (FK)
- product_id (FK)
- status (PENDING, SUCCESS, FAILED 등)
- createdAt

[Settlement]
- id (PK)
- order_id (FK)
- amount
- settledAt
```

---

## 핵심 기능 요약

### 🔐 사용자 인증
- 회원가입 / 로그인 API
- JWT 기반 인증 처리

### 🛍️ 상품 등록 및 이벤트 관리
- 한정 수량 상품 등록 (관리자 기능)
- 시작/종료 시간, 재고 수량 등 이벤트 설정

### 💳 결제 처리
- Redis 기반 분산 락으로 중복 결제 방지
- Kafka를 활용한 비동기 결제 요청 처리
- 결제 성공 시 MySQL 저장

### 📊 정산 처리
- Kafka Consumer가 결제 데이터를 수신
- 사용자/가맹점 기준 정산 내역 생성 및 저장

---

## 성능 테스트 (JMeter 기반)

- **부하 조건**: 초당 500건의 결제 요청 전송
- **결과**:
    - 평균 처리 성공 수: 480~490건
    - 성공률 약 95% 이상

> 테스트 결과는 Redis 락과 Kafka 큐 구조가 고트래픽 상황에서도 유효하게 작동함을 입증합니다.

---

## 개발 순서

1. ERD 및 Entity 설계
2. Redis 락 기능 구현
3. Kafka Producer/Consumer 구축
4. 사용자/상품/결제/정산 API 구현
5. Swagger 문서 자동화 구성
6. JMeter 성능 테스트 진행
7. README 및 프로젝트 문서화

---

## 유의 사항

- 재고가 모두 소진되거나 이벤트 시간이 초과된 경우, 결제 요청은 자동 실패 처리됩니다.
- 중복 요청 또는 병렬 요청에 대해서는 Redis 분산 락으로 제어합니다.

---
