package project.bookrental.management;

import java.io.Serializable;
import java.text.DecimalFormat;

public class BookIdDTO implements Serializable { // BookIdDTO 클래스 객체는 BookInfoDTO 클래스 객체를 참조해야 함.
	
	private static final long serialVersionUID = 2657950511453325629L;
	private String isbn;                     // 국제표준도서번호 
	private String bookid;                   // 도서아이디
	private BookInfoDTO bookInfo;        // 도서정보객체
	private boolean rentalAvailable = true;  // 대여가능여부 true(비치중) false(대여중)
	
	public BookIdDTO() { }
	
	public BookIdDTO(String isbn, String bookid, BookInfoDTO bookInfo) {
		this.isbn = isbn;
		this.bookid = bookid;
		this.bookInfo = bookInfo;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getBookid() {
		return bookid;
	}

	public void setBookid(String bookid) {
		this.bookid = bookid;
	}
	
	public BookInfoDTO getBookInfo() {
		return bookInfo;
	}

	public void setBookInfo(BookInfoDTO bookInfo) {
		this.bookInfo = bookInfo;
	}

	public boolean isRentalAvailable() {
		return rentalAvailable;
	}

	public void setRentalAvailable(boolean rentalAvailable) {
		this.rentalAvailable = rentalAvailable;
	}

	
	@Override
	public String toString() {
		String rental = "";
		if(rentalAvailable) {
			rental = "비치중";
		}
		else {
			rental = "대여중";
		}
		

		// BookRentalCtrl 의 객체 생성
		BookRentalCtrl ctrl = new BookRentalCtrl();
		
		// DecimalFormat 의 객체 생성 및 가격 포맷 바꾸기
		DecimalFormat df = new DecimalFormat("#,###"); 
		String sPrice = (String)df.format(bookInfo.getPrice());
		
		return ctrl.addHypenISBN(isbn)+"    "+ctrl.addHypenBookId(bookid)+"    "+bookInfo.getBookname()+"    "+bookInfo.getAuthorname()+"    "+bookInfo.getPublisher()+"    "+sPrice+"원"+"    "+rental; 
	}
	
}
