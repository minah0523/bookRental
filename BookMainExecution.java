package project.bookrental.management;

import java.util.Scanner;

public class BookMainExecution {

	public static void main(String[] args) { 

		Scanner sc = new Scanner(System.in);

		String sMenu = "      ===> 도서대여 프로그램 <=== \n"+
				       "1.사서 전용메뉴    2.일반회원 전용메뉴    3.프로그램 종료\n" +
                       "=> 메뉴번호선택 : ";
		
		String sMenuno = "";
		
		InterBookRentalCtrl ctrl = new BookRentalCtrl();
		
		do {
			System.out.println("");
			System.out.print(sMenu); // 메인 메뉴 보여주기
			sMenuno = sc.nextLine(); // 메인 메뉴번호 선택하기
			
			switch (sMenuno) {
				case "1":  // 사서전용메뉴
					ctrl.librarianMenu(sc);	
					break;
			
				case "2":  // 일반회원 전용메뉴
					ctrl.rentalUserMenu(sc);
					break;
				
				case "3":  // 프로그램종료
					break;
	
				default:
					System.out.println(">>> 메뉴에 없는 번호 입니다. 다시 선택하세요!!");
					break;
			}// end of switch------------------
			
		} while (!"3".equals(sMenuno)); // end of while-----------------
		
		System.out.println(">>> 프로그램을 종료합니다. <<<");
		sc.close();
		
	}// end of main()--------------------------------------------

}
