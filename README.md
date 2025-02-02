# 🍕🐨 PizzaKoala

## **What Keeps Me Going**

**PizzaKoala**는 사용자가 매일 조금씩 앞으로 나아가고 있음을 느낄 수 있도록 돕는 웹 서비스입니다.  
좋아하는 활동을 기록하고, 사진을 업로드하며, 꾸준히 이어온 날들을 캘린더에 시각적으로 확인할 수 있습니다. 마치 GitHub의 잔디처럼, 사용자가 걸어온 길을 한눈에 볼 수 있도록 디자인되었습니다.

## **✨ Features**

- ✅ **Daily Tracking** – 사용자가 올린 게시물이 캘린더에 기록되어 시각적으로 확인 가능  
- ✅ **Photo & Text Logging** – 사진과 글을 통해 현재 몰입하는 활동 기록  
- ✅ **Streak System** – 꾸준히 실천할 수 있도록 동기 부여  
- ✅ **Community & Sharing** – 다른 사용자와 공유하며 서로 응원
- ✅ **Real-Time Notifications (SSE)** – 실시간으로 알림을 받을 수 있는 SSE(서버 전송 이벤트) 시스템 제공

## **🚀 Why PizzaKoala?**

우리 삶에서 중요한 것은 거창한 목표보다 **매일 조금씩 앞으로 나아가는 것**입니다.  
어제보다 오늘, 오늘보다 내일 더 나은 내가 되기 위한 작은 루틴을 만들고 싶다면, **PizzaKoala**와 함께하세요.  

Website: <br>

## **🔧 Technical Stack**

- **Backend**: Spring Boot, Spring Security
- **Database**: MySQL
- **Authentication**: JWT (Access Token, Refresh Token)
- **OAuth Login**: Google Login
- **Real-Time Notifications**: SSE (Server-Sent Events) for real-time alerts
- **Frontend**: Swagger API for Backend Interaction

## **🚀 How to Use**

### **Swagger UI 사용하기**

1. **Swagger UI 접속**  
   **PizzaKoala**는 **Swagger UI**를 통해 제공되는 API 문서에서 백엔드 API를 테스트하고 확인할 수 있습니다. Swagger UI에 접속하면 **각 API 엔드포인트**를 한눈에 볼 수 있으며, 직접 요청을 보내고 응답을 확인할 수 있습니다.

![image](https://pizzakoala.s3.ap-northeast-2.amazonaws.com/2025-01-293.51.11-ezgif.com-speed.gif)


2. **API 인증**  
   Swagger UI 화면 상단의 **"Authorize"** 버튼을 클릭하여 인증을 진행합니다.  
   - **JWT 인증**을 선택하고 발급받은 **Access Token**을 입력합니다.
   - **Google 로그인** 또는 **OAuth2** 인증을 사용하면 자동으로 인증이 완료됩니다.

3. **API 엔드포인트 사용**  
   인증이 완료되면, API 요청을 **테스트**할 수 있는 버튼들이 활성화됩니다. 각 API 엔드포인트를 선택하여 테스트하고 응답을 확인할 수 있습니다.

**참고**: 인증된 사용자만 호출할 수 있는 API는 **Bearer Token**을 통해 인증을 거쳐야 합니다.



## **🛠️ What's in the Project**

- **API 설계 및 구현**  
  프로젝트 내의 모든 API 엔드포인트를 설계하고 구현했습니다. 주요 기능으로는 **기본로그인, 구글로그인**, **게시물 CRUD**, **댓글 기능**, **좋아요 시스템**, **실시간 알림 (SSE)**, **팔로우 기능**, **검색 기능**, **캘린더 기능** 등이 포함됩니다.

- **JWT 인증 시스템**  
  **JWT**를 활용하여 사용자 인증 및 권한 관리를 구현했습니다. **Access Token**과 **Refresh Token**을 이용하여 세션 관리 및 보안을 강화했으며, **Google 로그인** 기능도 지원합니다.

- **SSE 기반 실시간 알림 시스템**  
  **SSE**를 사용하여 실시간 알림 시스템을 구현했습니다. 이를 통해 사용자 간의 상호작용에 대한 실시간 알림을 제공하고, 더욱 동적인 경험을 제공합니다.
  - 실시간 알림 서비스
  - 알림 리스트 조회
  - 알림 단건 삭제
  - 알림 전체 삭제

- **MySQL 데이터베이스 설계 및 관리**  
  **MySQL**을 사용하여 게시물, 댓글, 좋아요 등의 데이터를 효율적으로 관리할 수 있도록 설계했습니다.

- **로그인 없이 이용 가능한 기능**  
  비회원 사용자도 **메인 페이지에서 좋아요가 많은 게시글**을 확인하고, **키워드 검색**을 통해 **좋아요 순으로 게시물**을 조회할 수 있습니다. 이를 통해 로그인하지 않고도 인기 있는 콘텐츠를 손쉽게 찾아볼 수 있도록 했습니다.

- **키워드 검색 기능**  
  사용자는 **게시물**과 **유저**를 키워드로 검색할 수 있습니다.  
  - 게시물 검색은 **좋아요 순**이나 **최신순**으로 정렬하여 조회할 수 있습니다.  
  - 유저 검색은 **팔로워가 많은 순** 또는 **최신 글을 올린 사람** 순으로 정렬할 수 있습니다.  
  이를 통해 사용자는 자신이 원하는 콘텐츠나 유저를 다양한 방식으로 쉽게 찾을 수 있습니다.

- **팔로우 기능**  
  - 사용자는 다른 사용자들을 **팔로우**하거나 **언팔로우**할 수 있습니다.  
  - 자신의 **팔로워 목록**과 **팔로잉 목록**을 조회할 수 있습니다.  
  - 나를 팔로잉하는 불편한 사용자가 나를 팔로우하는 것을 **제거**할 수 있습니다.

- **캘린더 기능**  
  사용자가 게시물을 올린 날마다 **캘린더에 자동으로 기록**됩니다.  
  - **월별 검색**: 특정 **년월**을 선택하여 한 달치 게시물 기록을 확인할 수 있습니다.  
  - **연도별 검색**: 특정 **년도를 선택**하여 전체적인 게시물 기록을 연 단위로 볼 수 있습니다.
    
![Calendar UI](https://pizzakoala.s3.ap-northeast-2.amazonaws.com/calender.png)

*이미지는 캘린더 UI 예시입니다.*


## **👾 My Portfolio**

더 자세한 정보와 다른 프로젝트들은 제 포트폴리오에서 확인하실 수 있습니다:  
[**My Portfolio**](#)

또는 GitHub 리포지토리에서 제 코드를 확인하실 수 있습니다:  
[**GitHub**](#)

## **📚 Learning & Challenges**

- **Spring Boot와 Spring Security의 통합**  
  프로젝트의 백엔드를 구축하면서 Spring Boot와 Spring Security를 활용하여 보안을 강화하는 방법을 배웠습니다. 특히 JWT 기반 인증 시스템을 설계하는 과정에서 보안적인 측면에 대해 많이 학습했습니다.

- **실시간 알림 시스템 설계**  
  처음으로 **SSE**를 사용하여 실시간 알림 기능을 구현하면서, 서버와 클라이언트 간의 데이터 흐름에 대해 깊이 이해하게 되었습니다. 클라이언트에서 실시간 알림을 처리하는 부분에서 많은 도전이 있었으나, 다양한 자료를 참고하며 해결할 수 있었습니다.


## **🔮 Future Improvements**

- **사용자별 주제별 게시판 기능**  
  현재는 하나의 게시판만 존재하지만, 향후 사용자가 여러 개의 게시판을 소유하고, 각 게시판에 맞는 주제를 설정하여 사진을 업로드하고 관리할 수 있는 기능을 추가할 예정입니다. 예를 들어:
  - 운동 사진을 올리는 게시판
  - 공부한 내용을 기록하는 게시판
  - 건강한 음식을 도전하며 사진을 올리는 게시판 등
  이렇게 다양한 주제의 게시판을 추가함으로써 사용자가 자신만의 카테고리로 활동을 기록하고 관리할 수 있습니다.

- **프론트엔드 리팩토링**  
  현재는 Swagger UI에서 API를 테스트하지만, 더 나은 사용자 경험을 제공하기 위해 실제 프론트엔드를 구축하여 사용자가 더욱 직관적으로 서비스를 사용할 수 있도록 리팩토링을 고려하고 있습니다.

- **성능 최적화 (비용 절감)**  
  나중에 웹사이트를 만들 때, **서버 비용 절감**을 위해 성능 최적화를 적용할 예정입니다. 데이터베이스 최적화와 캐싱 등을 통해 자원을 효율적으로 사용하고 비용을 절감할 수 있도록 할 것입니다. 이때 **Redis**를 사용하여 **인덱싱**과 **캐싱**을 통해 성능을 극대화할 예정입니다.

- **대댓글 기능 추가**  
  댓글에 대댓글 기능을 추가하여 사용자 간의 대화를 더욱 원활하게 할 계획입니다.

- **비밀번호 저장 기능**
  **비밀번호 저장** 옵션을 추가하여 사용자가 다시 로그인할 때 자동으로 입력되도록 개선할 계획입니다.

