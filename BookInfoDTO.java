package project.bookrental.management;

import java.io.Serializable;

public class BookInfoDTO implements Serializable {

	private static final long serialVersionUID = 5798143243448858074L;
	private String isbn;           // 국제표준도서번호 
	private String category;       // 도서분류카테고리
	private String bookname;       // 도서명
	private String authorname;     // 작가명
	private String publisher;      // 출판사
	private int price;             // 가격 
	
	public BookInfoDTO() { }
	
	public BookInfoDTO(String isbn, String category, String bookname, String authorname, String publisher,
			int price) {
		this.isbn = isbn;
		this.category = category;
		this.bookname = bookname;
		this.authorname = authorname;
		this.publisher = publisher;
		this.price = price;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBookname() {
		return bookname;
	}

	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	public String getAuthorname() {
		return authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getRentalPrice() { // 대여료  도서정가의 5% 로 책정함.
		return (int) (price*0.05);
	}
	
}
