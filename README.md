# FlashPay

## 📌 프로젝트 소개
**FlashPay**는 대규모 트래픽이 몰리는 한정 수량 이벤트(예: 콘서트 티켓, 한정판 상품 등)에서 빠르고 안정적인 결제 처리를 목표로 설계된 실시간 결제 처리 시스템입니다.  
Redis 기반의 분산 락과 Kafka 기반의 비동기 메시지 처리 구조를 통해 **결제 중복 방지**, **고성능 처리**, **정확한 정산**을 실현합니다.

---

## 🛠 기술 스택

| 영역       | 기술 및 도구                                     |
|------------|--------------------------------------------------|
| Language   | Java 17, Gradle                                  |
| Framework  | Spring Boot, Spring Security, Spring WebMVC      |
| 인증       | JWT (JSON Web Token), Spring Security             |
| 데이터베이스 | MySQL, Redis                                     |
| 메시징     | Apache Kafka                                      |
| 인프라     | Docker, Spring Cloud Config                       |
| 문서화     | Swagger (OpenAPI 3.0, springdoc-openapi)          |
| 테스트     | JMeter (성능 테스트)                              |

---

## 📁 폴더 구조

```bash
FlashPay/
├── src/
│   ├── main/
│   │   ├── java/com/flashpay/flashpay/
│   │   │   ├── config/        # 보안, Kafka, Redis 등 설정
│   │   │   ├── controller/    # REST API 컨트롤러
│   │   │   ├── domain/        # JPA 엔티티 클래스
│   │   │   ├── dto/           # 요청/응답 DTO
│   │   │   ├── service/       # 비즈니스 로직
│   │   │   ├── repository/    # JPA 인터페이스
│   │   │   ├── util/          # JwtUtil 등 유틸성 클래스
│   │   │   ├── common/        # ApiResponse 등 공통 객체
│   │   │   └── exception/     # 예외 처리 핸들러
│   └── resources/
│       ├── application.yml
│       └── static/
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
- status (PENDING, SUCCESS, FAILED)
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
• 회원가입 및 로그인 기능 제공
• JWT 기반 인증 처리
• 인증 필터를 통한 보안 처리 (JwtAuthenticationFilter, JwtUtil)


### 🛍️ 상품 등록 및 이벤트 관리
• 관리자 권한으로 상품 등록 가능
• 전체 상품 목록 조회 API 제공

### 💳 결제 처리
• Redis 분산 락을 활용하여 중복 결제 방지
• Kafka를 통해 주문 메시지를 비동기 처리
• 주문 요청은 빠르게 응답하고, 정산은 안정적으로 처리

### 📊 정산 처리
• Kafka Consumer가 주문 메시지를 수신하여 정산 데이터 생성
• 사용자 또는 상품 기준 정산 내역, 총 정산 금액, 최신 정산 정보 제공

---

### ⚙️ 시스템 아키텍처
```
sequenceDiagram
    participant Client
    participant API
    participant Redis
    participant Kafka
    participant Consumer
    participant DB

    Client->>API: 결제 요청 (userId, productId)
    API->>Redis: 분산 락 시도
    Redis-->>API: 락 획득 결과
    API->>Kafka: 주문 메시지 전송
    Kafka-->>Consumer: 메시지 수신
    Consumer->>DB: 정산 정보 저장
```


## 성능 테스트 (JMeter 기반)

• 테스트 조건: 초당 500건 주문 요청 전송
• 평균 성공 처리 수: 480~490건
• 성공률: 약 95% 이상

> Redis 락 + Kafka 처리 구조가 고트래픽에서도 안정적으로 작동함을 검증했습니다.

---

## 개발 순서

1.	ERD 설계 및 Entity 구성
2.	JWT 기반 인증 구현
3.	Redis 락 기능 개발
4.	Kafka Producer / Consumer 구성
5.	주문 및 정산 API 구현
6.	Swagger 문서 자동화 구성
7.	JMeter 기반 성능 테스트
8.	README 및 프로젝트 문서화

---

## 유의 사항

• 재고가 0이거나 이벤트 시간이 초과된 경우 결제는 자동으로 실패 처리됩니다.
• 병렬/중복 요청은 Redis 분산 락을 통해 제어됩니다.

---
