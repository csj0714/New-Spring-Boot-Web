## 목차

## 의뢰 내용

1. 문자 전송 기능
    - 매칭신청시 문자전송 & 매칭수락시 문자전송
2. 상대방 매칭 기능(매일 n시)
    - 소개팅 매칭에 필요한 정보(직업,지역,학과,군대,흡연여부,키,체형)
    - 단, 직업&지역은 필수일치 (나머지는 2개이상)
3. 개인정보(양방향) 및 비밀번호(단방향) 암호화
4. 이미지 처리
    - 저장은 잘 되지만, 웹 사이트에서 불러오지 못함.
5. 신청받은 유저 목록 조회 로직 수정
6. 밋팅 매칭 기능
    - 밋팅에 필요한 정보(평균나이, 직업, 밋팅지역, 밋팅분위기, 학과)
    - 최소 3가지 이상 만족필요, "밋팅지역"은 필수
7. 매일 n시마다 "새로운 상대가 기다리고 있습니다" 등의 문자 전송 기능
8. Dev/Prod 환경 분리

## 기타 작업 내용

### 1. 기존 개발 DB/API 서버와 별도로 상용 DB/API 서버 작성

- 개발 API 서버 주소 : http://54.180.82.158:8090
- 상용 API 서버 주소 : http://54.180.82.158:18090
- 개발 DB
    - jdbc-url : jdbc:mysql://myspringbootdb.ctq80qyc01oi.ap-northeast-2.rds.amazonaws.com:
      3306/myspringbootdb?allowPublicKeyRetrieval=true
    - username=csj5801
    - password=s2794105!
- 상용 DB
    - jdbc-url : jdbc:mysql://54.180.82.158:3306/prodDB?allowPublicKeyRetrieval=true
    - username=csj5801
    - password=s2794105!

### 2. Nginx (웹서버) 설정을 이용해 이미지 라우팅 처리 수정

- 문제점
    - /var/www/uploads 경로에 이미지를 저장 중.
    - 기존의 이미지를 불러오기 위해 아래와 같이 요청중인데, 이는 API를 호출하는 것에 해당됨. → 따라서 해당 API는 없기에 404 Not Found 발생
      ```shell
      GET http://54.180.82.158:18090/files/ff781851-6fdc-4d52-a5d4-48a49fe2d174.png
      ```
- 개선
    - Nginx를 설치 및 이미지 경로를 라우팅하도록 설정함
    - 서버 주소에 `/files` 라는 경로로 요청이 들어올 경우, `/var/www/uploads/`로 접근하도록 라우팅
    - `/etc/nginx/sites-available/default` 파일 참고

- 수정해야 할 점
    - 아래와 같이 {서버주소}:{포트번호}/files/{파일이름} 으로 접근하지 말고,
      ```shell
      GET http://54.180.82.158:18090/files/ff781851-6fdc-4d52-a5d4-48a49fe2d174.png
      ```
    - 포트번호를 제외하여 {서버주소}/files/{파일이름}로 접근할 것
      ```shell
      GET http://54.180.82.158/files/ff781851-6fdc-4d52-a5d4-48a49fe2d174.png
      ```

## 배포 프로세스

### DEPLOY

```shell
scp -i {pem 키 위치}/sshKey2.pem ./target/test-0.0.1.jar ubuntu@54.180.82.158:/home/ubuntu
```

### START

```shell
# 개발 API 서버
sh start-dev.sh

# 상용 API 서버
sh start-prod.sh
```

### STOP

```shell
sh stop-dev.sh
sh stop-prod.sh
```

### 로그 모니터링

```shell
# 개발 서버 로그 모니터링
sh log_dev.sh

# 상용 서버 로그 모니터링
sh log_prod.sh
```
