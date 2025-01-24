# Take-It

![TAKE-IT-박람회장-뒷면](https://github.com/user-attachments/assets/005c0821-a859-4264-8ac3-bdd46da52e2f)
## **🙌 프로젝트 소개**
한정판 상품의 신속하고 공정한 구매를 위한 플랫폼

**Take It**은 **대규모 트래픽 환경**에서도 안정적으로 **한정 상품 판매**를 제공하는  
MSA 기반 **이커머스 프로젝트**입니다.

## **프로젝트 목표**
- **MSA** 구조를 통해 서비스의 확장성과 유연성을 확보
- **대규모 트래픽 대응**
    - Redis Sorted Set을 활용한 대기열을 통해  부하 분산 및 사용자 경험 증진
    - Redis 캐싱을 활용하여 DB부하를 줄이고 응답 속도 확보
    - Redis + Lua Script을 활용한 동시성 처리로 안정성 있는 서비스 구축
- **운영 및 배포 효율화**
    - Docker, Github Actions, AWS Copilot을 이용한 CI/CD 파이프라인 구축으로 배포 자동화
    - Prometheus, Grafana, Loki를 활용한 실시간 메트릭 및 로그 모니터링으로 시스템 안정성 확보

## **기술 스택**

### **Backend**
<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"><img src="https://img.shields.io/badge/POSTGRESQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"><img src="https://img.shields.io/badge/REDIS-FF4438?style=for-the-badge&logo=redis&logoColor=white"><img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"><img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"><img src="https://img.shields.io/badge/jsonwebtokens-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"><img src="https://img.shields.io/badge/springDataJPA-90E59A?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/JUnit5-FB4F14?style=for-the-badge&logo=JUnit5&logoColor=white"><img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=RabbitMQ&logoColor=white">

### **DevOps**
<img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"><img src="https://img.shields.io/badge/amazonecs-FF9900?style=for-the-badge&logo=amazonecs&logoColor=white"><img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"><img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"><img src="https://img.shields.io/badge/dockerhub-2496ED?style=for-the-badge&logo=docker&logoColor=white"><img src="https://img.shields.io/badge/githubactions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"><img src="https://img.shields.io/badge/apachejmeter-D22128?style=for-the-badge&logo=apachejmeter&logoColor=white"><img src="https://img.shields.io/badge/prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white"><img src="https://img.shields.io/badge/grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white"><img src="https://img.shields.io/badge/loki-FFC61C?style=for-the-badge&logo=&logoColor=white"><img src="https://img.shields.io/badge/promtail-FFC61C?style=for-the-badge&logo=&logoColor=white"><img src="https://img.shields.io/badge/zipkin-F46800?style=for-the-badge&logo=&logoColor=white"><img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"><img src="https://img.shields.io/badge/aws fargate-FF9900?style=for-the-badge&logo=awsfargate&logoColor=white"><img src="https://img.shields.io/badge/aws copilot-000000?style=for-the-badge&logo=&logoColor=white">

### **Tools**
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"><img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"><img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white"><img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white"><img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"><img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">

## **주요 기능**

### 주문 시퀀스 다이어그램
![takeit drawio](https://github.com/user-attachments/assets/61bfe9be-6317-414d-9045-7b1af83567c1)
- TTL 설정을 통해 주문 요청 후 5분 동안 결제 미진행 시, 주문 자동 취소


### Redis 를 통한 캐싱 및  동시성 제어

- 자주 호출되는 데이터에 대해 캐싱하여 DB부하를 줄이고 응답속도 증가
- Redis + Lua Script을 통한 재고관리로 동시성 제어

### Redis Sorted Set 기반 대기열 구현

- Redis Sorted Set과 TTL 설정으로 주문 대기열 구현
- 동시 요청 수를 제한하여 평균 처리량 증가 및 데이터베이스 부하 감소

### 분산 시스템에서의 보상 트랜잭션

- 주문/결제 실패 시 RabbitMQ를 통해 비동기 복구 작업 수행
- 시스템 상태를 일관성 있게 유지하도록 구현

### 배포 자동화와 모니터링 시스템 구축

- Gradle Multi Module, Docker를 이용하여 Jar 및 컨테이너 빌드 환경 구성
- Github Actions를 이용하여 CI/CD 파이프라인 구축으로 빌드 및 배포 자동화
- AWS Copilot을 이용하여 ECS Fargate에 배포함으로써 인프라 구성 자동화 및 유연한 컨테이너 실행 환경 구성 
- Prometheus, Grafana, Loki, Promtail을 활용한 실시간 메트릭 및 로그 모니터링으로 시스템 안정성 확보

## ERD
![image](https://github.com/user-attachments/assets/eda73c4e-c2d5-4c23-9617-1cbec793476b)

## 인프라 설계
![image](https://github.com/user-attachments/assets/f100a676-d838-4fff-8b07-84c4f72bdcf5)


## 기술적 의사결정 / 트러블 슈팅
[Take It Wiki](https://github.com/take-it-14/take-it-backend/wiki)

## 팀 소개

| 이름   | 포지션   | 담당(개인별 기여점)                                                                                                                             | Github 링크                                |
|--------|----------|-----------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------|
| 김원겸 | 리더     | ▶ 게이트웨이 환경 구축 <br>▶ 찜, 결제, 카테고리 도메인 개발<br>▶ 상품, 유저 도메인에 레디스 캐싱<br>▶ Redis Lua Script를 통한 재고 관리 동시성 제어<br>▶ 찜한 상품 오픈날 사용자에게 이메일 전송 기능 개발 | [🍁 깃헙링크](https://github.com/rua0704)    |
| 김해나 | 팀원     | ▶ 인증, 사용자, 상품 도메인 개발<br>▶ **인프라**: AWS 환경 구축<br/>▶ **CI/CD**: Github Actions, AWS Copilot을 활용한 파이프라인 구성<br/>                                | [🍁 깃헙링크](https://github.com/gogohaena)                              |
| 이민정 | 팀원     | ▶ **주문**: MSA 기반 주문 로직 구현, RabbitMQ 기반 보상 트랜잭션 적용                                                                                       | [🍁 깃헙링크](https://github.com/M1ngD0ng)   |
| 최영근 | 팀원     | ▶ 리뷰, 쿠폰 도메인 개발<br>▶ Redis Sorted Set 기반 대기열 기능 개발<br>▶ RabbitMQ 기반 보상 트랜잭션 적용<br>▶ S3에 상품, 리뷰 이미지 업로드 기능 개발                            | [🍁 깃헙링크](https://github.com/ykchoi1203) |
