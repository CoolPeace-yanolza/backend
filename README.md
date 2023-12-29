# 쿨피스 팀 백엔드 레포지토리

쿨피스 팀의 백엔드 레포지토리입니다. JAVA Spring Boot REST API 서버를 개발 중입니다.

## ✨실행환경 설정 방법

아래 실행환경 설정은 **Java 17 버전**과 **docker**가 개인 개발 환경에서 설치되어 있다는 가정 하에 작성되었습니다.

1. **`.env` 파일을 만들어야 합니다.**

env 파일은 다음 [env 예제 파일(`.env.example`)](/.env.example)의 형식을 참고해주세요.

```properties
# MYSQL 설정
# !주의: USERNAME에 root를 입력하시면 안됩니다. root 외의 다른 이름을 입력해주세요.
MYSQL_USER=<개인 개발 환경의 MYSQL DB username>
MYSQL_PASSWORD=<개인 개발 환경의 MYSQL DB password>
MYSQL_VOLUME_PATH=./bin/mysql # MySQL 데이터를 저장할 개인 개발 환경의 경로
MYSQL_URL=localhost # 개인 개발 환경에서 사용할 MySQL URL
MYSQL_PORT=3306 # 개인 개발 환경에서 사용할 MySQL PORT

# JWT 설정
JWT_SECRET_KEY=<your-jwt-secret-key> # BASE64로 인코딩된 JWT 시크릿 키
JWT_ACCESS_EXPIRATION=36000000 # 액세스 토큰의 만료시간 (기본 1시간)
JWT_REFRESH_EXPIRATION=864000000 # 리프레시 토큰의 만료시간 (기본 24시간)

# Redis 설정
LOCAL_REDIS_VOLUME_PATH=./bin/redis # REDIS 데이터를 저장할 개인 개발 환경의 경로
LOCAL_REDIS_PORT=6379 # 개인 개발 환경에서 사용할 REDIS PORT
```

2. `docker compose`를 사용하여 mysql과 redis 데이터베이스 환경을 만들어야 합니다.

인텔리제이의 docker plugin 기능을 사용하시거나, 다음 명령어를 입력해서 데이터베이스 환경을 구성하세요.

```bash
docker compose up -d
```
