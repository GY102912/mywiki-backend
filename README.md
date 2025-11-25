# Community

## 개요

**Community**는 사람들과 온라인으로 자유롭게 소통하고 싶은 사용자를 위한 **커뮤니티 서비스**입니다.

게시글 작성, 댓글 작성, 게시글 좋아요를 통해 내 이야기를 나누고 다른 사용자의 이야기에 공감해보세요.

## 기능

- 게시글, 댓글 작성 및 조회와 게시글 좋아요 기능
- Spring Security & JWT & Redis 기반의 안전하고 확장성 높은 사용자 인증 절차
- Rate Limiting으로 과도한 요청 혹은 공격으로부터 시스템 보호
- 서버 로컬 디스크에 업로드된 파일을 링크 기반으로 조회 및 출력

## 디렉토리 구조

```yaml
application
├── exception
├── mapper
├── service
└── util
domain
├── converter
├── dto
├── event
├── exception
├── model
└── repository
global
├── config
├── exception
├── filter
├── provider
└── util
infrastructure
├── config
├── disk
├── exception
├── persistence
└── redis
presentation
├── controller
├── dto
├── util
└── validation
```

## 로컬 실행 방법

1. 저장소 클론
    
    ```bash
    git clone https://github.com/GY102912/mywiki-backend.git
    cd mywiki-backend
    ```
    
2. 프로젝트 루트에 .env 파일 작성
    
    ```
    # MySQL
    MYSQL_URL=jdbc:mysql://localhost:3306/your_schema?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    MYSQL_USER_NAME=your_user_name
    MYSQL_PASSWORD=your_password
    
    # Redis
    REDIS_HOST=localhost
    REDIS_PORT=6379
    
    # JWT
    JWT_SECRET=openssl rand -base64 32 명령어 결과값
    ```
    
3. MySQL & Redis 실행
4. CommunityApplication 실행
    
    ```bash
    ./gradlew bootRun
    ```

## 의존성

- Spring Boot 3.x
- Spring Security
- Spring Data JPA, QueryDSL
- Redis
- MySQL 8.0
- JJWT (JWT 토큰 인증)
- Bucket4j (API Rate Limiting)
- SpringDoc OpenAPI (Swagger UI)
