# inhadewbob

> 예산 기반 메뉴 추천 및 가계부 관리 플랫폼입니다. 입력한 예산, 카테고리 등의 조건을 고려하여 뽑기 기계를 돌리고 조건에 맞는 메뉴를 추천합니다.
> 대학생들의 공강 식비 절약을 돕고자 제작하였습니다.

---

## 📋 목차

- [기술 스택](#기술-스택)
- [시작하기](#시작하기)
- [환경 변수](#환경-변수)
- [API 명세](#api-명세)
- [프로젝트 구조](#프로젝트-구조)

---

## 기술 스택

- **언어**: Java 17
- **프레임워크**: Spring Boot 3.x
- **데이터베이스**: MySQL 8.x (AWS RDS)
- **인증**: JWT
- **서버**: AWS EC2

---

## 시작하기

### 사전 요구사항

- Java 17+
- Gradle

### 설치 및 실행

```bash
# 1. 레포지토리 클론
git clone https://github.com/Ganadi-Lab/Inhadewbob-BE.git
cd inhadewbob

# 2. 환경 변수 설정 (아래 환경 변수 섹션 참고)

# 3. 빌드 및 실행
./gradlew clean build
./gradlew bootRun
```

### EC2 배포

```bash
# 빌드
./gradlew clean build

# EC2에 jar 파일 업로드 후 실행
java -Duser.timezone=Asia/Seoul -jar inhadewbob-0.0.1-SNAPSHOT.jar
```
---

## 환경 변수

로컬 실행 시 아래 환경 변수를 OS 환경 변수 또는 IDE Run Configuration에 등록하세요.

| 변수명 | 설명 |
|---|---|
| `DEWBOB_DB_USERNAME` | RDS DB 유저명 |
| `DEWBOB_DB_PASSWORD` | RDS DB 비밀번호 |
| `DEWBOB_JWT_SECRET` | JWT 서명 키 |

---

## API 명세

| Method | Endpoint | 설명 | 인증 |
|---|---|---|---|
| `POST` | `/auth/login` | 로그인 (JWT) | X |
| `POST` | `/auth/signup` | 회원가입 | X |
| `POST` | `/auth/refresh` | 토큰 재발급 | X |
| `GET` | `/auth/profile` | 회원 정보 조회 | O |
| `GET` | `/menus/roulette?date={date}&category={category}&price={price}` | 랜덤 식당 메뉴 조회 (룰렛) | O |
| `POST` | `/diets` | 식단 기록 등록 | O |
| `GET` | `/diets/daily?date={date}` | 일별 식단 기록 조회 | O |
| `DELETE` | `/diets/{id}` | 식단 기록 삭제 | O |
| `POST` | `/consumes` | 소비 현황 등록 | O |
| `GET` | `/consumes/status?date={date}` | 소비 현황 조회 | O |

---

## 프로젝트 구조

```
src/main/java/GanadiLab/inhadewbob/
├── domain/
│   ├── consume/          # 섭취 기록
│   │   ├── api/
│   │   ├── dto/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── diet/             # 식단
│   │   ├── api/
│   │   ├── dto/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── manualDietDetail/ # 수동 식단 상세
│   │   ├── model/
│   │   └── repository/
│   ├── member/           # 회원
│   │   ├── model/
│   │   └── repository/
│   ├── menu/             # 메뉴
│   │   ├── api/
│   │   ├── dto/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── restaurant/       # 식당
│       ├── model/
│       └── repository/
└── global/
    ├── auth/             # 인증 처리
    ├── base/             # 공통 모델 및 유틸
    ├── config/           # Security 등 설정
    ├── jwt/              # JWT 유틸리티
    └── newAuth/          # 토큰 발급/갱신
        └── token/
            ├── api/
            ├── dto/
            ├── exception/
            └── service/
```
