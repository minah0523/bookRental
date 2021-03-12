package project.bookrental.management;

import java.util.Scanner;

public interface InterBookRentalCtrl {

	void Return_Main(Scanner sc); // 메인으로 돌아가기

	void librarianMenu(Scanner sc); // 사서 전용 메뉴
	
	void rentalUserMenu(Scanner sc); // 일반회원 전용 메뉴
	
	void registerLibrarian(Scanner sc); // 사서가입(Map)
	
	boolean isUsableLibID(String libid); // 사서 아이디 중복 검사하기 
	
	LibrarianDTO loginLib(Scanner sc); // 사서로 로그인 하기
	
	void registerBookInfo(Scanner sc); // 도서정보등록(Map) 
	
	boolean isUsableISBN(String isbn); // 국제표준도서번호(ISBN) 중복 검사하기 

	String removeHypen(String ISBN); // ISBN, 도서ID 하이픈 없애기

	String addHypenISBN(String r_isbn); // ISBN에 하이픈 추가하기
	
	void registerBookId(Scanner sc); // 대여도서객체(List)등록 
	
	boolean isUsableBookId(String bookid); // 중복 도서아이디 검사하기 
	
	void registerRentalUser(Scanner sc); // 대여자객체(List)등록 
	
	boolean isUsableUserId(String userid); // 중복 회원아이디 검사하기 
	
	UserDTO loginRentalUser(Scanner sc); // 일반회원으로 로그인 하기
	
	void searchBook(Scanner sc); // 도서 검색하기 
	
	String addHypenBookId(String LOT); // bookid에 하이픈 추가하기

	void rental(Scanner sc); // 도서대여하기
	
	void rentAllView();  // 사서는 대여중인 도서에 대해 모든 정보를 조회할 수 있도록 한다.
	
	void rentMyView(String userid);   // 대여자는 자신이 대여한 도서대여 정보를 조회할 수 있도록 한다.
	
	void bookReturn(Scanner sc); // 도서반납하기 
	
}
