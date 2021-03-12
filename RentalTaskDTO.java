package project.bookrental.management;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RentalTaskDTO implements Serializable {

	private static final long serialVersionUID = -8559314580501367051L;
	private String userid;               // 회원아이디
	private String bookid;               // 도서아이디
	private String rentalDay;            // 대여일자
	private String scheduledReturnDay;   // 반납예정일자
	private int arrears;                 // 연체료
	private UserDTO rtuser;           // 회원객체
	private BookInfoDTO bookinfo;    // 도서정보객체
	
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getBookid() {
		return bookid;
	}

	public void setBookid(String bookid) {
		this.bookid = bookid;
	}

	public String getRentalDay() {
		return rentalDay;
	}

	public void setRentalDay() { // 대여일자는 현재일로 설정한다. 
		SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy-MM-dd");
	//	rentalDay = sdfmt.format(new Date()); // 날짜를 String 타입으로 변경시키기.
	//  대여일은 현재날짜(yyyy-MM-dd)로 한다. 이것이 정상적인 행동임. 
		
		// === 연체료를 물기 위하여 일부러 대여일자를 5일전으로 만들어 보겠다. === //
		Calendar calendar = new GregorianCalendar(Locale.KOREA); // Calendar 객체 생성
		calendar.setTime(new Date());           // Date를 [new Date() == 현재시각] Calendar 에 맵핑시키기 
		calendar.add(Calendar.DAY_OF_YEAR, -5); // 현재날짜로 부터 -5일을 더한다.
		
		rentalDay = sdfmt.format(calendar.getTime()); // 대여일자를 일부러 5일전으로 만든 것이다.
	}

	public String getScheduledReturnDay() {
		return scheduledReturnDay;
	}

	public void setScheduledReturnDay() { // 반납예정일은 대여일로 부터 3일 뒤로 설정한다.
		try {
			// 먼저 대여일은 String 타입이므로 대여일을 Date 타입으로 변경시킨다. 
			SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dRentalDay = sdfmt.parse(rentalDay); // String 타입을 Date 타입으로 변경시키기 
			
			Calendar calendar = new GregorianCalendar(Locale.KOREA); // Calendar 객체 생성
			calendar.setTime(dRentalDay);          // 반납예정일을 Calendar 에 맵핑시키기 
			calendar.add(Calendar.DAY_OF_YEAR, 3); // 반납예정일로 부터 3일을 더한다.
			
			scheduledReturnDay = sdfmt.format(calendar.getTime()); // 날짜를 문자열로 변경시키기
		} catch (ParseException e) {
			System.out.println("~~~ 반납예정일은 날짜모양을 띄는 String 타입이어야 합니다.");
		}
	}

	public int getArrears() {
		return arrears;
	}

	public void setArrears() {
		try {
			 SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy-MM-dd"); 
			 String currentDay = sdfmt.format(new Date());
			
			 Date currentDate = sdfmt.parse(currentDay);
			 Date scheduledReturnDate = sdfmt.parse(scheduledReturnDay);
			
			 long diff = currentDate.getTime() - scheduledReturnDate.getTime();
			 // Date 객체의 getTime()은 1970-01-01 00:00:00 으로 부터 경과한 밀리초임.
			 // currentDate.getTime()은 1970-01-01 00:00:00 으로 부터 현재일 까지 경과한 밀리초임. 
			 // scheduledReturnDate.getTime()은 1970-01-01 00:00:00 으로 부터 반납예정일 까지 경과한 밀리초임.
	         
			 // 시간차이를 시간,분,초, 1000을 곱한값으로 나누면 하루단위가 나온다.
	         long diffDays = diff/(24*60*60*1000);
			 
	         if(diffDays >= 1) {
	        	 arrears = ((int)diffDays) * 100;
	         }
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String sArrears() {
		DecimalFormat df = new DecimalFormat("#,###");
		String sArrears = (String)df.format(arrears);
		return sArrears+"원";
	}
	
	public UserDTO getRtuser() {
		return rtuser;
	}

	public void setRtuser(UserDTO rtuser) {
		this.rtuser = rtuser;
	}

	public BookInfoDTO getBookinfo() {
		return bookinfo;
	}

	public void setBookinfo(BookInfoDTO bookinfo) {
		this.bookinfo = bookinfo;
	}
	
	
	@Override
	public String toString() {
		
		// BookRentalCtrl 의 객체 생성
		BookRentalCtrl ctrl = new BookRentalCtrl();
		
		String str = ctrl.addHypenBookId(bookid) + "   " + 
					 ctrl.addHypenISBN(bookinfo.getIsbn()) + "   " + 
		             bookinfo.getBookname() + "   "+ 
		             bookinfo.getAuthorname() + "   " +
		             bookinfo.getPublisher() + "   " + 
		             userid + "   " +
				     rtuser.getName() + "   " +
		             rtuser.getPhone() + "   " + 
				     rentalDay + "   " + 
		             scheduledReturnDay;
				
		return str; 
	}
	
	
	
}
