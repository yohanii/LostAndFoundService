## 기록

- 23.12.10
  - post edit 기능 추가
    - user.name과 post.user.name이 동일할 때만 수정 가능
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
