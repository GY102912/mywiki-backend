# Community

## 개요

---

**Community**는 사람들과 온라인으로 자유롭게 소통하고 싶은 사용자를 위한 **커뮤니티 서비스**입니다.

게시글 작성, 댓글 작성, 게시글 좋아요를 통해 내 이야기를 나누고 다른 사용자의 이야기에 공감해보세요.

## 기능

---

- 게시글, 댓글 작성 및 조회와 게시글 좋아요 기능
- Spring Security & JWT & Redis 기반의 안전하고 확장성 높은 사용자 인증 절차
- Rate Limiting으로 과도한 요청 혹은 공격으로부터 시스템 보호
- 서버 로컬 디스크에 업로드된 파일을 링크 기반으로 조회 및 출력

## 로컬 실행 방법

---

1. git clone
2. 프로젝트 루트에 .env 파일 생성
    1. MySQL 설치 후 스키마 생성하여 `MYSQL_URL`, `MYSQL_USER_NAME`, `MYSQL_PASSWORD`작성
    2. Redis 설치 후 스키마 생성하여 `REDIS_HOST`, `REDIS_PORT` 작성
    3. openssl rand -base64 32 명령어로 시크릿 키 생성하여 `JWT_SECRET` 작성
3. MySQL, Redis 서버 실행
4. CommunityApplication 실행

## 의존성

---

- Spring Boot 3.x
- Spring Security
- Spring Data JPA, QueryDSL
- Redis
- MySQL 8.0
- JJWT (JWT 토큰 인증)
- Bucket4j (API Rate Limiting)
- SpringDoc OpenAPI (Swagger UI)
