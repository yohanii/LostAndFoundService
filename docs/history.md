## 기록

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
