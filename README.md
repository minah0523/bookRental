# Book Rental Program


**Book Rental Program은 Java 언어로 만든 도서관리 시스템 입니다.** 

- 콘솔에서 프로그램을 실행할 수 있으며, 회원, 도서, 대여 관련 데이터는 직렬화, 역직렬화를 이용하여 .dat 파일로 저장 및 활용합니다.


## 기술스택

- Java


## 개발 포인트

- DTO 클래스로 회원, 도서, 대여 정보를 관리 - *DTO.java
- [사서, 일반회원의 권한을 분리하여 접속할 수 있도록 하는 메인 컨트롤러](https://github.com/minah0523/bookRental/blob/main/BookMainExecution.java)
- [Stream을 이용하여 직렬화, 역직렬화로 파일 및 데이터를 관리](https://github.com/minah0523/bookRental/blob/main/BookRentalSerializable.java)
- [도서 정보 및 회원정보의 유효성 검사 및 ISBN 형식의 정규화, 데이터 입력 및 조회](https://github.com/minah0523/bookRental/blob/main/BookRentalCtrl.java)
