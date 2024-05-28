## 기록

- 24.5.28
  - view 수정
    - 전체적인 화면 수정
    - image 없을 시 default 보여주게 처리
  - edit Post 시 기존 사진 없어지는 문제 해결
  - post delete 안되는 문제 해결
  - unloginBodyHeader, bodyHeader to bodyHeader, loginBodyHeader
  - ItemCategory 기타 추가 & PostSaveDto Valid 조건 추가
  - LoginCheckInterceptor 적용 범위 수정
    - 로그인 화면에서 회원가입 화면 연결 & 회원가입 시 로그인 화면으로 redirect
      - HOTFIX: websocket url 설정
        - 문제 : 운영환경에서 websocket 이용한 chatting 안되는 문제
          - 해결
            1. websocket 생성자 url 변경
               - `"ws://localhost:8080/ws/chat"` -> `"ws://" + [[${url}]] + "/ws"`
               - WebSocketConfig paths도 바꿔줌. `"/ws/chat"` -> `"/ws"`
            2. nginx 포워딩 설정
               - ```
                 server {
                        server_name wanna-find.com www.wanna-find.com; # managed by Certbot
                        root         /usr/share/nginx/html;
                        
                        ...
              
                        location /ws {
                                proxy_pass http://localhost:8080/ws;
                                proxy_http_version 1.1;
                                proxy_set_header Upgrade $http_upgrade;
                                proxy_set_header Connection "Upgrade";
                                proxy_set_header Host $host;
                                proxy_set_header X-Real-IP $remote_addr;
                                proxy_set_header X-Forwarded-Host $host;
                                proxy_set_header X-Forwarded-Server $host;
                                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                        }
                        ...
                 }
                 ```
            3. Mixed content 문제 해결
               - https 사이트에서 http 사이트 요청 시 발생하는 보안 문제
               - header에 아래 코드 추가
               ```html
               <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
                ```
      
- 24.5.13
  - 디렉토리 구조 변경
    - product 별로 크게 분리함
- 24.5.2
  - local 모니터링 설정
    - Prometheus, Grafana 환경
    - 추후 운영 환경에도 적용할 예정
- 24.4.11
  - adminPage Members 권한 수정 구현
    - 체크박스 체크한 members의 권한을 ADMIN -> MEMBER, MEMBER -> ADMIN으로 수정
- 24.4.10
  - Member auth 생성
    - admin page에는 관리자만 입장 가능해야해서 만들게 되었다.
    - MemberAuth.ADMIN, MemberAuth.MEMBER 두 가지
  - Interceptor를 이용한 adminPage 접근 제한
    - Interceptor를 이용해 "/admin", "/admin/*"에 입장 시 auth를 체크해, MemberAuth.ADMIN일 때만 입장 가능.
- 24.4.4
  - admin page overview 구현
- 24.4.1
  - 관리자 페이지 구현
    - 회원, 게시글, 채팅룸 현황 화면 생성
    - delete Members, Posts, Rooms 구현
  - createdTime, updatedTime 제대로 기록되도록 refactoring
    - save시 createdTime, updatedTime 같아야한다.
    - room의 경우는 방 생성 후, ENTER 채팅이 보내져서, room의 createdTime, updatedTime 같지 않고, 해당 채팅 createdTime과 room의 updatedTime이 같다.
- 24.3.29
  - 맴버 삭제 불가능 문제 해결
    - Member 객체 삭제시 `Cannot delete or update a parent row: a foreign key constraint fails cascade` error 발생
    - 원인 : 다른 테이블에 해당 데이터를 참조하고 있는 외래키가 있어서 발생한 문제.
    - 시도
      - 연관관계의 부모에 `cascade = CascadeType.ALL, orphanRemoval = true`을 모두 추가해줌
        - `cascade = CascadeType.ALL` : 부모가 자식의 전체 생명 주기를 관리한다. 부모가 영속화되면, 자식도 영속화된다. + 부모객체가 삭제되면, 자식 객체도 삭제된다.
          단, 부모와 자식의 연관관계가 끊어질 경우, 자식을 미처 제거하지 못한다.
        - `orphanRemoval = true` : 부모와 자식의 연관관계가 끊어질 경우, 자식 엔티디들을 삭제한다.
      - 그러나, 계속 실패해서 자식측에 `optional = false` 추가
        - `optional = false` : 해당 객체가 null이 아님을 보장
      - 그래도 실패
    - 해결
      - JPQL로 쿼리를 날려서 delete한 것이 원인이었다. 그러면, cascade가 적용안된다.
      - `em.remove`를 통해 삭제해줘서 해결함.
  - `Member.notifications` FetchType.EAGER -> FetchType.LAZY 바꿈
    - 24.2.21에 nav에서 알림을 보여줄 때, `LazyInitializationException`이 발생했다.
    - FetchType.LAZY로 설정할 경우에 notifications를 들고 올 수 없어서, 당시엔 FetchType.EAGER로 설정해서 해결했었다.
    - 하지만, member를 조회할 때마다 notifications도 같이 조회하기 때문에 비효율적이었다.
    - 그러다가 최근에 Transaction 관련 강의를 들으면서 새로운 해결법을 알게되었다.
    - 기존엔 template에서 notifications를 조회했기 때문에, Transaction이 걸려있지 않아서 지연로딩을 할 수 없어던 것이다.
    - 그래서, FetchType.LAZY로 바꿔주고, notifications는 Transaction이 걸려있을 때 가져올 수 있도록, NotificationService에서 받아오게 해서 해결했다.
- 24.3.25
  - Nginx를 이용한 포트포워딩
  - Let's Encrypt를 이용한 HTTPS 사용
- 24.3.14
  - 이미지 s3에 저장,다운로드 구현
    - 기존 file로 local에 저장하는 방식에서, s3 사용으로 전환
- 24.3.6
  - application.yml로 변환
    - 설정파일의 가독성을 높이기위해서 application.properties에서 application.yml로 변환해주었다.
  - 설정파일 profile 설정
    - 임시로 profile을 local로 설정해두었고, 설정파일을 로컬&개발과 운영으로 local, prod로 나누어서 관리할 예정이다.
  - test AutoConfigureTestDatabase annotation 제거
    - `IllegalStateException: Failed to load ApplicationContext..` 에러가 떠서 살펴봤더니, `file.dir`을 가져오지 못하고있었다.
    - main의 application.properties에서 더 이상 가져오지 못해서 그런 것이였고, main과 test의 설정파일이 제대로 분리되었다는 것을 보여주는 것이다.
    - test application.yml에 `file.dir`을 설정해주면서 해결되었다.
    - 이제 AutoConfigureTestDatabase annotation을 붙이지 않아도, h2 db를 제대로 사용하기 때문에 제거해주었다.
  - 운영/로컬 설정파일 분리
    - application.yml에서 profile 설정해준다.
      - `application-{spring:profiles:active(local/prod)}.yml` 설정파일에 레벨별로 환경변수 설정
      - Intellij Edit Configurations의 Active Profiles를 `local`로 설정
    - application-local.yml, application-prod.yml로 분리
      - application-prod.yml에는 RDS url, username, password가 적혀야하는데, 보안때문에 변수로 설정하고, ec2에서 환경변수를 설정한다.
      - ec2에 shell 시작될 때마다 실행되도록 `vi ~/.bashrc`를 실행하고 `.bashrc` 파일에 적어둔다.

- 24.2.25
  - Hikari Connection 문제
    - `HikariPool-1 - Connection is not available, request timed out after 30007ms.`
    - 상황
      - 로그인 상태에서 새로고침 몇 번하거나, 페이지 이동 몇 번하면 멈추면서 위의 에러 발생함.
      - hikariPool에서 이용 가능한 커넥션이 없어서 Thread가 handOffQueue에서 기다리다가 30초가 지나서 발생하는 것으로 보임.
    - 원인
      - eventSource로 `/notifications/subscribe/[[${member.id}]]`경로를 받을 때, 해당 경로로 GetMapping이 나가서 `NotificationService.subscribe`가 매번 실행되고 있음.
    - 해결 1
      - `/notifications/accept/{id}`경로의 GetMapping 만들어서, EmitterRepository의 Map에 저장되어있는 SseEmitter 가져다주게 했는데,
      동작 안됨. -> 실패
    - 해결 2
      - SSE 통신을 하는 동안은 HTTP Connection이 계속 열려 있어서, JPA 사용시 open-in-view 속성이 true 이면 DB Connection도 지속된다고 한다.
      - 그래서, Connection이 부족했고, open-in-view 속성 false로 했다.
      - Connection 부족 문제는 해결되었으나, 지연로딩(LAZY)가 안 됨. -> 실패
    - 결과
      - 알림 기능 임시로 꺼둔 상태이다.

- 24.2.21
  - 로그아웃 상태에서도 notification 저장되도록 수정
    - `NotificationService.notify` 로직 수정
  - Member, Notifiacation Entity 수정
    - `Member` <> `Notification` : OneToMany
    - `Member` : `notifications` 추가, `@OrderBy("createdTime desc")` 어노테이션 추가
    - `Notification` : `createdTime` 추가
  - 알림 목록 구현
    - `memberId`에 해당하는 Notifications 가져와서 보여준다.
  - Member Entity에 FetchType 설정
    - nav에서 알림 보여줘야하기 때문에, `Member`의 `notifications`에 `fetch = FetchType.EAGER` 설정

- 24.2.19
  - 채팅방 생성 알림 기능 구현
    - 새로운 채팅 요청 들어올 시 alert 발생
    - SSE(Server-Sent-Event) 사용
    - `NotificationType`은 `GENERAL, CHATTING` 두 가지 종류
    - 로그인 시 Emitter 생성하고, `NotificationType.GENERAL` 타입의 더미 이벤트 보낸다.
      - 아무런 이벤트도 보내지 않으면 재연결 요청을 보내거나, 연결 요청 자체에서 오류가 발생하기 때문.
      - EventListener로 event 받아서, 보여줌
      - SSEEmitter 안 올라가 있으면, 어떤 알림도 받을 수 없음. 로그아웃 상태에서도 알림 저장되도록 추후 수정 예정.
    - 유저가 '채팅하기' 버튼을 통해, 새로운 채팅 방 개설 시, 상대방 MemberId로 `NotificationType.CHATTING` 타입의 event를 `SseEmitter::send`
    - templates/fragments의 header, loginHeader 분리

- 24.2.16
  - findRoom 관련 test들 작성
  - entity 수정
    - Post (One) <-> Room (Many)
    - Member.rooms 제거
  - 채팅방, 채팅 목록 제대로 연결 안 된 문제 해결
    - RoomRepository, RoomService에 member가 속한 Room들 찾는 methods 구현
    - 찾은 Room List 객체를 넘겨줘, 채팅 목록 보여줌

- 24.2.15
  - chatting, room tests 추가
  - chatting JavaScript code를 fragment화
  - chatting history 보여주기 구현
  - 채팅 목록 구현
    - `Member.rooms`로 객체 전달. 그런데, 해당 member가 `room.partnerId`에 해당하는 경우는 채팅 목록을 볼 수 없는 문제 있음. 

- 24.2.14
  - 일대일 chatting 기능 구현
    - chat은 webSocket을 사용하기 때문에, 관련 Config, Handler 추가
    - Chatting, Room의 Controller, Service, Repository 구현
    - Room에 `storeRoomName`을 추가해, Room 생성 시 임의의 random UUID를 할당.
    Chatting room GetMapping url에 사용된다.
    

- 24.2.9
  - entity 연관관계 cascade 추가
    - Item -> Image, Member -> Image, Post -> Item : CascadeType.ALL
  - 게시물 화면에서 아이템, 이미지 보기 구현
  - post 최신순으로 보여주기
    - PostRepository 해당하는 쿼리에 `order by p.createdTime desc` 추가
  - post 추가 시 post type 자동 설정
    - `redirectURL`를 활용해,  Lost에서 접근 시 `PostType.LOST`, Found에서 접근 시 `PostType.FOUND`
  - 게시글 아이템 이미지 수정 구현
    - 수정 요청 시, 기존 아이템 이미지 모두 제거 후, dto로 받은 이미지로 저장

- 24.2.7
  - 게시글 아이템, 이미지 등록 구현
    - 이제 게시물 추가 시, 아이템 정보도 입력
    - 아이템엔 프로필 이미지와 달리, 이미지 여러 장 추가 가능
    - ItemRepository 추가
    - ItemRepository, ImageStoreService 추가된 함수에 대한 테스트 작성
  - image file 여러 개 미리보기 구현
    - JS
    ```javascript
    function readURLs(e){
      var files = e.files;
      var fileArr = Array.prototype.slice.call(files)
      document.getElementById("previews").innerHTML = "";

      for(f of fileArr){
        imageLoader(f);
      }
    }
    
    function imageLoader(input) {
      if (input) {
        var reader = new FileReader();
        var previews = document.getElementById("previews");

        reader.onload = function(e) {
          let img = document.createElement('img')
          img.src = e.target.result;
          previews.appendChild(img);
        };
        reader.readAsDataURL(input);
      }
    }
    ```
    - `readURLs()`에서 파일들을 파일로 나눠서 `imageLoader()` 실행. 파일 개수만큼 실행.
    - `imageLoader()`에서 src 넣어주고, 보여주기
    - script 코드는 fragment로 만들어서 활용 ex) `<head th:replace="fragments/imageJSScript :: imageJSScript">`

- 24.2.6
  - 프로필 이미지 수정 구현
    - Controller -> Service에서 `ProfileEditRequestDto`를 이용
    - `ImageStoreService.saveImage`에서 ProfileImage update도 해준다. save와 많은 코드가 겹쳐서 재활용 했다.

- 24.2.5
  - 회원가입 화면, 프로필 수정 화면에서 이미지 미리보기 구현
    - JS
    ```javascript
      function readURL(input) {
        if (input.files && input.files[0]) {
          var reader = new FileReader();
          reader.onload = function(e) {
            document.getElementById('preview').src = e.target.result;
          };
          reader.readAsDataURL(input.files[0]);
        } else {
          document.getElementById('preview').src = "";
        }
      }
      ```
    - HTML
    ```html
    <input type="file" id="profileImage" th:value="*{profileImage}" class="form-control" onchange="readURL(this);">
    ```
    - profile image 등록하면, `onchange`로 `readURL()` 실행
    - `readURL()`에서 등록한 Image file의 url을 src에 등록해줌으로서 Image 보여줌.
    - `e` : 이벤트 객체. ex) `ProgressEvent {isTrusted: true, lengthComputable: true, loaded: 10046, total: 10046, type: 'load', …}`
    - `e.target` : 이벤트가 발생된 대상. ex) `FileReader {readyState: 2, result: 'data:image/jpeg;base64,/…', error: null, onloadstart: null, onprogress: null, …}`
    - `e.target.result` : blob 등이 특수하게 가공된 URL (보통  MIME 타입 선언뒤  BASE64로 처리된 문자열 형식). ex) `'data:image/jpeg;base64,/…'`

- 24.2.1
  - 프로필 이미지 보기 구현
  - 예외처리 : 회원가입 시 profileImage 없을 때, 빈 Image 객체 저장되는 부분
  - 프로필에서 profileImage 없을 시, default Image 보여주기 구현

- 24.1.30
  - user -> member 이름 변경
    - h2 db에서 user가 예약어로 설정되어있어서, db에서만 users로 되도록 바꿨었는데, 이와 관련해 계속 문제가 생길 것 같아 member로 이름변경을 해줬다.
    - 미처 제대로 못 바꾼 부분은 계속 바꿀 예정
  - ImageStore 관련 service, dto 추가
    - Image는 MultipartFile로 받아서, 중간에서 Image로 변환해주는 작업 있다.
    - image file은 일단 로컬에 저장. 추후엔 s3에 저장할 예정.
  - 회원가입 시 userProfile 저장 기능 구현

- 24.1.27
  - test db 분리
    - 해결을 위해 3일 사용
    - 방법
      - build.gradle에 설정 추가
        - `runtimeOnly 'com.h2database:h2'` 추가
      - test resources에 따로 application.yml 추가
        - ```
          spring:
            datasource:
              url: jdbc:h2:mem:testdb;MODE=MYSQL;
              username: sa
              password:
              driver-class-name: org.h2.Driver
            jpa:
              hibernate:
                ddl-auto: create
              properties:
                hibernate:
                format_sql: true
            
          logging:
            level:
              org.hibernate.SQL: debug
          ```
        - url : `jdbc:h2:mem:{db이름}`으로 In-Memory mode로 사용한다. spring boot 2.1.10 이후로 `MODE=MYSQL`를 써줘야 MySQL을 인식한다.
        - In-Memory mode : 스프링 부트 실행할 때 함께 H2를 띄운다. H2 DB 데이터를 로컬에 저장하지 않고 메모리에만 가지고있다.
      - db를 사용하는 test class에 `@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)`를 붙여줌
        - 설정시, 테스트용 클래스에서 사용할 데이터베이스를 적용되게 하는 Annotation이다.

    - 문제
      - `Caused by: org.h2.jdbc.JdbcSQLSyntaxErrorException: Syntax error in SQL statement "insert into [*]user..` 발생
        - H2 데이터베이스 2.1.214 버전에서 user 키워드가 예약어로 지정되어 있어서 발생한 문제
        - User Entitiy에 `@Table(name = "users")` 써줘서 해결
      - `dialect: org.hibernate.dialect.MySQL5InnoDBDialect` 사용시 에러
        - `MySQL5InnoDBDialect`는 deprecated되었고 따라서 `MySQL57Dialect`을 사용해야 한다고 한다.
        - `MySQL8Dialect`이 있지만 아직 공식적으로 사용하는지 모르겠음
        - 아마 schema.sql로 table 설정을 해줘야 하는 것 같았고, 없어도 해결가능해서 사용 안 함
      - In-Memory mode가 아닌 Server mode로 했을 때 문제
        - 처음엔 url로 `jdbc:h2:tcp://localhost/~/test`를 사용해 Server mode로 사용하려 했었다.
          - 하지만, test용 h2 db를 쓰지 않고, 원래 db를 계속 참조하는 문제 해결을 못했고, In-Memory가 자료 접근이 훨씬 빠르기 때문에 갈아탔다.
          - 데이터 양의 빠른 증가로 데이터베이스 응답 속도가 떨어지는 문제를 해결할 수 있는 대안이 인 메모리 데이터베이스이다.

  - default 이미지 생성
    - 문제 : DB Image table에 항상 default image가 있었으면 좋겠음 
    - 해결 : `@EventListener(ApplicationReadyEvent.class)`을 사용해 application 시작할 때마다 db 체크하고 없을 경우 추가
      - `ApplicationReadyEvent`는 스프링 컨테이너가 모두 띄워졌을 때 발생하는 이벤트다.
      - 스프링 컨테이너가 모두 완전히 떴다는 의미는 스프링 AOP, 트랜잭션 등등 모든 것을 완성한 Applicaiton Context가 완성되었다는 의미이다. 
      - 따라서 init() 메서드를 호출하면, 프록시를 통해 만들어진 스프링 빈을 통해서 해당 메서드가 호출되기 때문에 트랜잭션이 적용된다.
      
- 24.1.23
  - 문제: posts, postSearch, MyPosts 모두 같은 템플릿을 사용하는데, postSearch 때문에 'PostSearchRequestDto', 'isSearch'를 항상 보내줘야한다.
    - 해결1: thymeleaf에서 no input이여도 가능한가?
      - 검색 결과 불가능이라고 판단
    - 해결2: posts, MyPosts / postSearch 이렇게 템플릿 2개로 분리하기로 결정
  - 마이페이지 구현 완료
    - 관심 목록은 추후에 개발 예정

- 24.1.22
  - LoginUserAOP 구현
    - 문제: nav의 마이페이지로 연결하기 위해 모든 controller에 user를 받아와야하나 고민
    - 해결: AOP에서 loginUser를 model에 담기
    - web.post, web.profile, web.user 내부의 GetMapping methods + web.HomeController + PostController.postSearch(PostMapping이라 따로 추가해줌)에 적용됨
    - PointCut을 따로 분리해놔서 재활용 가능하게 함
    - @Login 어노테이션으로 일일히 추가헀던 코드들 수정

- 24.1.16
  - 게시물 검색 기능 추가
    - JPQL로 쿼리 작성 (추후에 QueryDSL로 바꿀 예정)
    - 게시물 내용과 게시물 타입으로 검색

- 24.1.11
  - 누락된 test 추가
    - User.updateUser test, UserRepository.findByNickName test

- 24.1.10
  - nickName 중복 여러번 시 redirectURL 연결 안되는 문제 해결
    - redirectURL을 model에 저장해줌
  - 회원가입 시 닉네임 입력 구현
    - 중복 확인도 한다.

- 24.1.9
  - home과 post에서 name 대신 nickName으로 볼 수 있게 수정
  - home에서 nickName 변경 반영 안되는 문제 해결
    - HomeController에서 loginUser를 바로 보내지 말고, loginUser.id로 user 객체 찾아서 전송
  - nickName 중복 막고 error message 보여주기 구현
    - bindingResult.reject 사용
  - loginHome에 logout button 추가

- 24.1.7
  - profile 수정 구현
    - ProfileEditRequestDto 생성
    - PRG 패턴으로 edit-form에서 submit하면 post로 보내고, 통과 시 redirect get, 불통 시 edit-form 연결

- 24.1.5
  - profile 보기 구현
    - ProfileController 생성

- 24.1.3
  - 게시물 내용 tag를 input에서 textarea로 수정
    - tag를 input에 type='text'로 사용했었는데, 내용이 길어도 한 줄밖에 볼 수 없는 문제가 있어서 textarea로 바꿈. 

- 23.12.29
  - redirect url 연결 수정
    - post Create, Read, Update url에 이전 페이지 url을 redirect url로 붙여줌.
  - 작성자 이름 대신 loginId 보이게 하기
    - 추후에 nickName으로 바꿀 예정.

- 23.12.11
  - html 구조 수정
    - home.html 다시 만들고, postsLost, postsFound 두 경로로 따로 이어지도록 만듬.
    - fragments를 만들어 반복되는 부분 재활용하고자 함.

- 23.12.10
  - post edit 기능 추가
    - user.name과 post.user.name이 동일할 때만 수정 가능
    - edit를 PostMapping 대신 PatchMapping으로 받게끔, 아래 delete처럼 수정함.
  - post delete 기능 추가
    - user.name과 post.user.name이 동일할 때만 삭제 가능
    - html에서 delete 요청을 보내기 위해 아래와 같이 구성함.
    - ```html
      <form id="delete_form" th:action="@{/posts/{postId}(postId=${post.id})}" method="post">
          <input type="hidden" name="_method" value="delete"/>
      </form>
      <button form="delete_form" type="submit"> 게시물 삭제 </button>
      ```
    - hidden을 활용하는게 포인트. 대신 hidden을 제대로 받기 위해서는 application에 아래를 추가해줘야함.
    - ```java
      @Bean
      public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new HiddenHttpMethodFilter();
      }
      ```
      

- 23.12.08
  - createdTime 추가
    - Post와 User가 생성될 때, createdTime data가 입력되도록 하였다.
    - saveDTO의 toEntity가 호출되는 시점에, `LocalDateTime.now()`를 넣어주는 방식으로 구현하였다.
    - `"${#temporals.format(post.createdTime, 'yyyy-MM-dd')}"`를 통해 형식 조절할 수 있음.
    - Post, User의 생성자에 createdTime, updatedTime을 추가하였으며, builder를 쓸 때 원하는 것만 추가해도 된다는 것을 처음 알았다.
    - 그래서 조금의 test 수정도 같이 함.
    - 그리고 dto의 생성자가 필요없어서 제거해둠.


- 23.11.20
  - `@PostConstruct` 관련
    - 원래 `@PostConstruct`로 초기 유저와 초기 게시물들을 추가해줬었는데, 
      DB를 연결하면서 필요 없게 되었다. 오히려 서버 실행할 때마다, 추가되어서 문제였고, 그래서 삭제해주었다.
    - `@PostConstruct`에는 `@Transaction`이 안 먹혔다. 사용할 거면, 우회하는 방법을 사용해야한다.
      내 경우엔 Repository에 `@Transaction`을 걸어주는 방식으로 임시로 했었고, 검색해보면 우회하는 다른 방법들도 있다.
