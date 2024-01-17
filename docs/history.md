## 기록

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
