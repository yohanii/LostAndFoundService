## 기록

- 23.11.20
  - `@PostConstruct` 관련
    - 원래 `@PostConstruct`로 초기 유저와 초기 게시물들을 추가해줬었는데, 
      DB를 연결하면서 필요 없게 되었다. 오히려 서버 실행할 때마다, 추가되어서 문제였고, 그래서 삭제해주었다.
    - `@PostConstruct`에는 `@Transaction`이 안 먹혔다. 사용할 거면, 우회하는 방법을 사용해야한다.
      내 경우엔 Repository에 `@Transaction`을 걸어주는 방식으로 임시로 했었고, 검색해보면 우회하는 다른 방법들도 있다.
