# LostAndFoundService
![Java](https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring-Boot](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![JPA](https://img.shields.io/badge/jpa-00555?style=for-the-badge&logo=jpa&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

- 1인 개발
- 개발 기간 : 23.11.01 ~

## 📖 개요
  - 잃어버린 물건이나, 찾은 물건을 게시글로 올려 주인을 찾아주는 **분실물 서비스** 입니다.
  - TDD, OOP, Clean Code를 지향하며 개발 중 입니다.

## 🛠 개발 환경
- JAVA : 17
- Spring Boot : 3.1.5
- JPA : 3.1.5
- Thymeleaf   
- MySQL : 8.0.34
- Lombok : 1.18.20

## 📃 IA
![IA.png](docs/img/IA.png)

## 💾 ERD
![ERD.png](docs/img/ERDv3.png)

## 💡 아키텍쳐
![architecture_goal.png](docs%2Fimg%2Farchitecture_goal.png)
![architecture_current.png](docs%2Fimg%2Farchitecture_current.png)

## 🎯 진행 상황
![progress2.png](docs%2Fimg%2Fprogress2.png)

## 💎 기능 소개

- PRG 패턴을 사용해, POST 요청의 중복을 막음
- Interceptor 활용한 로그인 여부 체크 기능
  - 로그인 시 Session에 유저 객체가 담기기 때문에, LoginCheckInterceptor에서 Session을 확인해 로그인 여부를 체크
  - 비로그인 시, 로그인 화면으로 redirect
  - WebConfig에서 interceptor 추가 및 비로그인 상태에서 볼 수 있는 view 관리
- ArgumentResolver 활용한 로그인 회원 정보 조회
  - @Login 커스텀 어노테이션으로 로그인 회원 정보 조회 가능
  - @SessionAttribute 대신 커스텀 어노테이션으로 더 깔끔하고 편하게 사용 가능

## 📷 결과 화면
![view1.png](docs%2Fimg%2Fview1.png)
![view2.png](docs%2Fimg%2Fview2.png)
![view3.png](docs%2Fimg%2Fview3.png)
![view4.png](docs%2Fimg%2Fview4.png)
![view5.png](docs%2Fimg%2Fview5.png)
![view6.png](docs%2Fimg%2Fview6.png)
![view7.png](docs%2Fimg%2Fview7.png)