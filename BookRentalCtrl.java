package project.bookrental.management;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class BookRentalCtrl implements InterBookRentalCtrl {
	
	// 사서 회원 리스트를 기록한 파일
	private final String LIBRARIAN_FILE = "C:/iotestdata/project/bookrental/librarian.dat"; 
	// 도서 정보 리스트를 기록한 파일
	private final String BOOKINFOLIST_FILE = "C:/iotestdata/project/bookrental/bookinfolist.dat";
	// 대여 도서 정보 리스트를 기록한 파일
	private final String BOOKIDLIST_FILE = "C:/iotestdata/project/bookrental/bookidlist.dat";
	// 대여 고객 정보 리스트를 기록한 파일
	private final String USERLIST_FILE = "C:/iotestdata/project/bookrental/userlist.dat";
	// 도서 대여 업무 정보 리스트를 기록한 파일
	private final String RENTALTASKLIST_FILE = "C:/iotestdata/project/bookrental/rentaltasklist.dat";
	
	// 직렬화, 역직렬화(데이터 파일 저장 및 출력)를 위한 객체 생성
	private BookRentalSerializable serial = new BookRentalSerializable();
	
	// 메인 페이지로 돌아가게 할 수 있는 리셋 키를 설정
	String RESET_MSG = "\n초기 메뉴로 돌아가시려면 [*]를 눌러주세요!\n";
	String RESET_KEY = "*";
	
	// 메인 페이지로 보내주는 메서드 (* 입력시에 메인으로 돌아가게 하도록 활용)
	@Override
	public void Return_Main(Scanner sc) {
		BookMainExecution.main(null);
	}
	
	
	// ~~~~~~~~~~ 1. 사서 전용 메뉴를 만든다. ~~~~~~~~~~
	// == 사서 전용메뉴 == 
	@Override
	public void librarianMenu(Scanner sc) {
		
		// 로그인한 사서 DTO를 담을 lib 초기화
		LibrarianDTO lib = null;
		
		// 사용자가 입력할 String Menu no 초기화
		String sMenuno = "";
		
		// 로그인을 위한 do-while 문
		do {
			
			// 로그인 DTO가 null인경우 "", 아이디가 있을 경우 id 로그인중.. 로 표시
			String loginId = (lib==null)?"":"["+lib.getLbid()+" 로그인중..]";
			
			// 메뉴로 띄울 문구: 위에서 만든 loginId도 띄워준다
			String sMenu = "\n>>>> 사서 전용 메뉴 "+loginId+"<<<<\n" + 
				      "1.사서가입    2.로그인    3.로그아웃   4.도서정보등록   5.개별도서등록\n"+
					  "6.도서대여해주기    7.대여중인도서조회    8.도서반납해주기    9.나가기\n" + 
	                  "=> 메뉴번호선택 : ";
		
			// 상기 메뉴 문구를 콘솔에 출력한다
			System.out.print(sMenu);
		
			// 사서전용 Menu 메뉴번호 선택하기
			sMenuno = sc.nextLine(); 
		
			// 메뉴 선택에 따른 switch-case 문
			switch (sMenuno) {
				case "1":  // 사서가입
					registerLibrarian(sc);
					break;
					
				case "2":  // 로그인
					lib = loginLib(sc);
					break;
					
				case "3":  // 로그아웃
					lib = null;
					System.out.println(">>> 로그아웃 성공!!!");
					break;
					
				case "4":  // 도서정보등록
					if(lib != null) {
						registerBookInfo(sc);
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}
					break;
					
				case "5":  // 개별도서등록
					if(lib != null) {
						registerBookId(sc);
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}
					
					break;	
					
				case "6":  // 도서대여해주기
					if(lib != null) {
						rental(sc);
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}
					
					break;
					
				case "7":  // 대여중인 모든 도서조회
					if(lib != null) {
						rentAllView();
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}
					
					break;
					
				case "8":  // 도서반납해주기
					if(lib != null) {
						bookReturn(sc);  
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}

					break;	
					
				case "9":  // 나가기
					break;
				
				default:
					System.out.println("~~~~ 존재하지 않는 메뉴번호 입니다.!!");
					break;
			}	
			
		} while (!"9".equals(sMenuno));	
		
	}// end of public void librarianMenu(Scanner sc)----------------------------------------
	
	
	// ~~~~~~~~~~ 3. 사서객체(Map)를 파일에 저장한다. ~~~~~~~~~~ 
	// == 3.1 사서가입(Map) ==
	@SuppressWarnings("unchecked")
	@Override
	public void registerLibrarian(Scanner sc) {
		
		System.out.println("\n== 사서가입하기 ==" + RESET_MSG);
		String libid = "";
		
		// 사서ID 중복검사하기 
		do {
			System.out.print("▶ 사서ID: ");
			// 사용할 아이디를 받는다.
			libid = sc.nextLine();  // mina, admin, hongkd...
			
			// 유저가 *표를 입력한경우 메인메뉴로 리턴한다.
			keyReturnMain(libid, sc);
			
			// 만약 받은 id가 null이거나 공백, 빈칸인 경우 경고를 띄운다
			if(libid==null || libid.trim().isEmpty()) {
				System.out.println("~~~ 사서아이디를 입력하세요!!");
				continue;
			}
			// 만약 받은 아이디가 사용가능하지 않다면 (이미 존재한다면) 경고를 띄운다. 
			else if(!isUsableLibID(libid)) {
				System.out.println("~~~ "+libid+" 는 이미 존재하므로 다른 사서ID를 입력하세요!!");
				continue;
			}
			else {
				break;
			}
		} while (true);
				
		System.out.print("▶ 암호: ");  // 1234 , 1234
		String pwd = sc.nextLine();

		// 사서 DTO에 위에서 받은 아이디와 비밀번호를 넣는다
		LibrarianDTO librarian = new LibrarianDTO(libid, pwd);
				
		// LIBRARIAN_FILE 에 해당하는 파일객체 생성하기
		File file = new File(LIBRARIAN_FILE); 
		Map<String, LibrarianDTO> librarianMap = null;
		int n = 0;
		
		if(!file.exists()) { // 파일이 존재하지 않는 경우. 즉, 최초로 부서등록을 하는 경우이다.
			librarianMap = new HashMap<>();
			librarianMap.put(libid, librarian);
			n = serial.objectToFileSave(librarianMap, LIBRARIAN_FILE); // LIBRARIAN_FILE 파일에 librarianMap 객체를 저장시킨다.	
		}
		else { // 파일이 존재하는 경우. 두번째 이후로 부서가입하는 경우 기존의 Map에 HashMap을 추가해준다
			Object librarianMapObj = serial.getObjectFromFile(LIBRARIAN_FILE); // LIBRARIAN_FILE 파일에 저장된 객체를 불러온다.
			librarianMap = (HashMap<String, LibrarianDTO>) librarianMapObj;
			librarianMap.put(libid, librarian);
			n = serial.objectToFileSave(librarianMap, LIBRARIAN_FILE); // LIBRARIAN_FILE 파일에 librarianMap 객체를 저장시킨다.
		}
		
		if(n==1)
			System.out.println(">>> 사서등록 성공!! <<<");
		else
			System.out.println(">>> 사서등록 실패!! <<<");
		
	}// end of public void registerLibrarian(Scanner sc)-----------------------------------------

	
	// == 3.2 사서 아이디 중복 가능여부 검사하기 ==
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUsableLibID(String libid) {
		// 최초로 가입시 파일에 저장된 librarianMapObj 객체가 없기애 중복된 회원아이디가 없으므로 사용가능하도록 한다.
		boolean isUsable = true; 
		
		// 사서 파일의 정보를 Object 객체에 담는다
		Object librarianMapObj = serial.getObjectFromFile(LIBRARIAN_FILE); 

		// 파일에 저장된 librarianMapObj 객체가 있다면 
		if(librarianMapObj != null) { 
			HashMap<String, LibrarianDTO> librarianMap = (HashMap<String, LibrarianDTO>)librarianMapObj;
			LibrarianDTO lib = librarianMap.get(libid);
			if(lib != null) { // 사서아이디가 존재하는 경우라면 
				isUsable = false;
			}
		}
		return isUsable;
	}// end of public boolean isUseLibID(String libid)-----------------------------------	

	
	// ~~~~~~~~~~ 4. 사서로 로그인 하기 ~~~~~~~~~~ 
	@SuppressWarnings("unchecked")
	@Override
	public LibrarianDTO loginLib(Scanner sc) { // 사서로 로그인 하기
	
		LibrarianDTO lib = null;
		
		System.out.println("\n == 로그인 하기 == " + RESET_MSG); 
		
		System.out.print("▶ 사서아이디: ");
		String libid = sc.nextLine();
		
		// 유저가 *표를 입력한경우 메인메뉴로 리턴한다.
		keyReturnMain(libid, sc);
		
		System.out.print("▶ 암호: ");
		String pwd = sc.nextLine();
		
		Object librarianMapObj = serial.getObjectFromFile(LIBRARIAN_FILE); 
		
		// 파일로 부터 객체정보를 얻어온 경우라면   
		if(librarianMapObj != null) { 
			
			// 해당 Object를 HashMap 형태로 변환
			HashMap<String, LibrarianDTO> librarianMap = (HashMap<String, LibrarianDTO>) librarianMapObj;	
			
			// 사서 아이디로 정보를 get한다
			lib = librarianMap.get(libid);
			
			// 정보가 일치한다면
			if(lib != null && lib.getLbid().equals(libid) && lib.getPwd().equals(pwd)) {
				System.out.println(">>> 로그인 성공!!! <<<");
				return lib;
			}
			
			// 정보가 일치하지 않는다면
			else {
				System.out.println(">>> 로그인 실패. 아이디와 비밀번호를 확인하세요. <<<");
				return null;	
			}
			
		}
		else {
			System.out.println(">>> 등록된 사서정보가 없습니다. 회원가입을 먼저 진행해주세요.");
		}
		
		return lib;
		
	}// end of public Librarian loginLib(Scanner sc)------------------------------------ 
	
	
	
	// ~~~~~~~~~~ 5. 도서정보(ISBN)객체(Map)를 파일에 저장한다. ~~~~~~~~~~ 
	// == 도서정보등록(Map) ==
	@SuppressWarnings("unchecked")
	@Override
	public void registerBookInfo(Scanner sc) {
		
		System.out.println("\n== 도서정보 등록하기 ==" + RESET_MSG);
		
		String sc_isbn = ""; // "" 값으로 초기화
		String r_isbn = ""; // "" 값으로 초기화
		
		do {
			System.out.print("\n▶ 국제표준도서번호 (ISBN):");
			sc_isbn = sc.nextLine(); // 유저가 입력한 ISBN 값 
			keyReturnMain(sc_isbn, sc);

			r_isbn = removeHypen(sc_isbn);// 입력받은 문자에 하이픈 (-) 이 있다면 제거
			
			// ISBN 유효성 검사
			if (r_isbn == null || r_isbn.trim().isEmpty()) {
				System.out.println("공백 및 문자는 입력 할 수 없습니다." + "\n국제표준도서번호 (ISBN) (숫자) 을/를 입력하세요");
			} 
			else if (!isUsableISBN(r_isbn)) {
				System.out.println(addHypenISBN(r_isbn) + " 는 이미 존재하므로 다른 국제표준도서번호(ISBN)를 입력하세요!!!");
			} 
			else if (r_isbn.length() > 13) {
				System.out.println("ISBN 국제표준도서번호는 13자리를 초과할 수 없습니다.");
			} 
			else if (r_isbn.length() < 13) {
				System.out.println("ISBN 국제표준도서번호는 13자리로 입력해 주세요.");
			} 
			else {
				break;
			}
		} while (true);
		
		System.out.println("입력하신 ISBN(기호제거) :" + r_isbn);
		System.out.println("입력하신 ISBN의 자리수 : " + r_isbn.length());
		String a_isbn = addHypenISBN(r_isbn); // 하이픈을 없앤 ISBN 에 다시 하이픈을 붙여준다.
		System.out.println("표준화 ISBN(하이픈 포함): " + a_isbn);
		
		System.out.print("▶도서분류카테고리: ");  // Programming , DataBase ,  Programming
		String category = sc.nextLine();
		
		System.out.print("▶도서명: ");         // JAVA기초 , Oracle기초 , Spring 
		String bookname = sc.nextLine();
		
		System.out.print("▶작가명: ");         // 강감찬 , 장보고 , 유관순 
		String authorname = sc.nextLine();
		
		System.out.print("▶출판사: ");         // 쌍용출판사 , 강북출판사 , 세종출판사
		String publisher = sc.nextLine();
		
		// 유효성 검사
		int price = 0;
		do {
			System.out.print("▶ 가격 : ");     // 20000 , 25000 , 30000
			String sPrice = sc.nextLine();
			try {
				 price = Integer.parseInt(sPrice);
				 break;
			} catch(NumberFormatException e) {
				System.out.println("~~~~ 오류 : 가격은 숫자로만 입력하세요!!!\n");
			}
		} while (true);
				
		// 위에서 받은 모든 변수들을 BookInfoDTO에 넣어준다
		BookInfoDTO bookInfoDto = new BookInfoDTO(r_isbn, category, bookname, authorname, publisher, price);
				
		File file = new File(BOOKINFOLIST_FILE); // BOOKINFOLIST_FILE 에 해당하는 파일객체 생성하기
		Map<String, BookInfoDTO> bookInfoMap = null;
		int n = 0;
		
		if(!file.exists()) { // 파일이 존재하지 않는 경우. 즉, 최초로 부서등록을 하는 경우이다.
			bookInfoMap = new HashMap<>();
			bookInfoMap.put(r_isbn, bookInfoDto);
			n = serial.objectToFileSave(bookInfoMap, BOOKINFOLIST_FILE); // BOOKINFOLIST_FILE 파일에 bookInfoMap 객체를 저장시킨다.	
		}
		else { // 파일이 존재하는 경우. 두번째 이후로 부서가입시
			Object bookInfoMapObj = serial.getObjectFromFile(BOOKINFOLIST_FILE); // BOOKINFOLIST_FILE 파일에 저장된 객체를 불러온다.
			bookInfoMap = (HashMap<String, BookInfoDTO>) bookInfoMapObj;
			bookInfoMap.put(r_isbn, bookInfoDto);
			n = serial.objectToFileSave(bookInfoMap, BOOKINFOLIST_FILE); // BOOKINFOLIST_FILE 파일에 bookInfoMap 객체를 저장시킨다.
		}
		
		if(n==1)
			System.out.println(">>> 도서정보등록 성공!! <<<");
		else
			System.out.println(">>> 도서정보등록 실패!! <<<");
		
	}// end of public void registerbookInfo(Scanner sc)-----------------------------------------
	
	
	
	// 5-1. == 국제표준도서번호(ISBN) 중복 검사하기 ==
	@Override
	public boolean isUsableISBN(String isbn) {

		boolean isUsable = true; // 최초로 가입시 파일에 저장된 bookInfoMapObj 객체가 없기애 중복된 ISBN 이 없으므로 사용가능하도록 한다.
		
		Object bookInfoMapObj = serial.getObjectFromFile(BOOKINFOLIST_FILE); 
		
		if(bookInfoMapObj != null) { // 파일에 저장된 bookInfoMapObj 객체가 있다라면 
			
			@SuppressWarnings("unchecked")
			HashMap<String, BookInfoDTO> bookInfoMap = (HashMap<String, BookInfoDTO>)bookInfoMapObj;
			BookInfoDTO bookinfo = bookInfoMap.get(isbn);
			
			if(bookinfo != null) { // ISBN 존재하는 경우이라면 
				isUsable = false;
			}
		}
		return isUsable;
	}
	
	// == 5-2. ISBN에서 하이픈 없애기
	@Override
	public String removeHypen(String isbn) {
		// 숫자가 아닌것은 ""로 변경
		String r_isbn = isbn.replaceAll("[^0-9]", "");
		return r_isbn;
	}

	// 하이픈 다시넣기
	@Override
	public String addHypenISBN(String r_isbn) {
		// 13자리의 숫자를 123-45-67890-12-3 의 형태로 변경
		String a_isbn = r_isbn.replaceFirst("(^|[0-9]{3})([0-9]{2})([0-9]{5})([0-9]{2})([0-9]{1})$", "$1-$2-$3-$4-$5");
		return a_isbn;
	}
	
	
	
	
	// ~~~~~~~~~~ 6. 개별도서객체(List)를 파일에 저장한다. ~~~~~~~~~~ 
	// == 6.1 개별도서객체(List) 등록 ==
	@SuppressWarnings("unchecked")
	@Override
	public void registerBookId(Scanner sc) {
		
		System.out.println("\n== 개별도서 등록하기 ==" + RESET_MSG);
		
		System.out.print("▶ 국제표준도서번호(ISBN): ");  // 111-22-33333-44-5
		
		// 등록할 개별도서의 ISBN을 사용자에게 받는다
		String sc_isbn = sc.nextLine();
		
		// *표 입력시 메인메뉴로 돌아간다
		keyReturnMain(sc_isbn, sc); 

		// 하이픈을 없앤 isbn인 r_isbn 변수 초기화
		String r_isbn = "";
		
		// 하이픈 제거
		r_isbn = removeHypen(sc_isbn);

		// 개별도서 아이디 중복 검사하기 
		// ISBN이 등록되지 않은경우 (ISBN 신규등록이 가능하다면 아직 등록되지 않은 것)
		if (isUsableISBN(r_isbn)) {
			System.out.println(">>> 해당 ISBN은 아직 등록되지 않았습니다. ISBN을 먼저 등록 후 개별도서 등록이 가능합니다. <<<");
			return;

		} else { // 신규등록이 가능하지 않다면 해당 ISBN이 기록되어있는 상태

			// BOOKINFOLIST_FILE 파일에 저장된 도서정보객체(Map) 정보를 가져온다.
			Object bookInfoMapObj = serial.getObjectFromFile(BOOKINFOLIST_FILE);

			// 도서 ISBN 정보를 Map으로 변환
			Map<String, BookInfoDTO> bookInfoMap = (HashMap<String, BookInfoDTO>) bookInfoMapObj; 
			
			BookInfoDTO bookInfoDto = bookInfoMap.get(r_isbn);

			String sc_bookid = "";
			String r_bookid = "";
			
				do {
					// 도서아이디를 입력받는다 (숫자 16자리)
					System.out.print("▶  도서아이디 : ");
					sc_bookid = sc.nextLine();
					
					// *표 입력시 메인메뉴로 돌아간다
					keyReturnMain(sc_bookid, sc); 

					// 하이픈 등 기호를 없애고 숫자만 남긴다
					r_bookid = removeHypen(sc_bookid);

					// 만약 r_bookid가 null이거나 공백 또는 입력하지 않은경우
					if (r_bookid == null || r_bookid.trim().isEmpty()) {
						System.out.println("~~~ 숫자 16자리 도서아이디를 입력하세요!!");
						break;
					// 만약 id가 이미 기록되어 있는경우
					} else if (!isUsableBookId(r_bookid)) {
						System.out.println("~~~ 추가하신 " + addHypenBookId(r_bookid) + " 는 이미 다른 도서에서 사용중 입니다. \n 다른 아이디를 입력하세요.");
						break;
					// 만약 id의 길이가 16자리를 초과한경우
					} else if (r_bookid.length() != 16) {
						System.out.println("~~~ ISBN 을 포함한 개별 아이디는 16자리로 입력해 주세요.");
						break;
					// 만약 ISBN과 아이디의 앞부분이 동일하지 않다면
					} else if(!r_bookid.substring(0, 13).equals(bookInfoDto.getIsbn())) {
						System.out.println("~~~ 앞 13자리는 ISBN을 입력하셔야 합니다!!");
						break;
					}
					// bookInfoDto 변수에 해당 DTO가 저장되어있는 상태이다.
					
					// 새로운 bookIdDto를 생성해준다
					BookIdDTO bookIdDto = new BookIdDTO(r_isbn, r_bookid, bookInfoDto);
					
					// BOOKIDLIST_FILE을 불러온다
					File file = new File(BOOKIDLIST_FILE);
					
					// bookIdList 리스트용 변수를 초기화한다
					List<BookIdDTO> bookIdList = null;

					int n = 0;
					
					// 파일이 존재하지 않는 경우. 즉 최초로 도서입력시 
					if(!file.exists()) { 
						//bookIdList에 새 리스트를 만든다
						bookIdList = new ArrayList<>();
						// 새로운 bookIdDto를 추가한다
						bookIdList.add(bookIdDto);
						// 해당 리스트를 BOOKIDLIST_FILE로 저장한다
						n = serial.objectToFileSave(bookIdList, BOOKIDLIST_FILE); 	
					}
					// 파일이 존재하는 경우. 두번째 이후로 도서입력시
					else {
						// 기존의 데이터를 Object의 형태로 변환한다
						Object bookIdListObj = serial.getObjectFromFile(BOOKIDLIST_FILE); 
						// 해당 Object를 리스트 형태로 변환한다
						bookIdList = (List<BookIdDTO>) bookIdListObj;
						// 기존의 리스트에 새로운 bookIdDto를 추가한다
						bookIdList.add(bookIdDto);
						// 해당 리스트를 BOOKIDLIST_FILE로 저장한다
						n = serial.objectToFileSave(bookIdList, BOOKIDLIST_FILE);
					}
					if(n==1)
						System.out.println(">>> 도서등록 성공!! <<<");
					else
						System.out.println(">>> 도서등록 실패!! <<<");
					
					// 등록을 마친 후 break로 do-while문을 빠져나온다.
					break;

				} // end of do
				while(true);
				
		}// end of else (신규등록이 가능하지 않다면 해당 ISBN이 기록되어있는 상태)------------------
		
	}// end of public void registerbookId(Scanner sc)------------------------------------	

	

	// BookId 신규생성이 가능한지 확인하는 메서드
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUsableBookId(String r_bookid) {
		
		// 사용가능한지 알려주는 변수 초기화 (파일이 없는경우에는 사용가능하므로 true로 초기화함)
		boolean isUsable = true;
		
		// BOOKIDLIST_FILE을 역직렬화 하여 Lib 변수에 넣는다.
		Object Lib = serial.getObjectFromFile(BOOKIDLIST_FILE);

		// 만약 Lib Object가 존재한다면
		if (Lib != null) {
			// Lib Object를 BookIdDTO 리스트 형태로 변환한다.
			List<BookIdDTO> bookIdDtoList = (List<BookIdDTO>) Lib;
			// bookIdDtoList 각각의 BookIdDTO를 비교한다
			for (BookIdDTO bookIdDto : bookIdDtoList) {
				// 만약 
				if (r_bookid.equals(bookIdDto.getBookid())) {
					isUsable = false;
					break;
				}
			}
		}
		return isUsable;
	}

	

	// ~~~~~~~~~~ 10. 도서대여하기  ~~~~~~~~~~ 
	// 대여자는 도서대여를 위해 대여도서를 사서에게 건네주면 
	// 사서는 대여목록에서 대여자의 미반납된 도서중 반납예정일이 지난 미반납된 도서가 있다라면 
	// 도서대여를 불가하다라는 메시지가 출력되도록 한다. 반납예정일은 대여일로 부터 3일뒤가 되도록 한다. 
	@SuppressWarnings("unchecked")
	@Override
	public void rental(Scanner sc) { // 도서대여하기 
		
		System.out.println("\n >>> 도서대여하기 <<<" + RESET_MSG);
		
		String userid = "";  // 도서를 대여해갈 회원ID
		do {
			System.out.print("▶ 회원ID: ");
			userid = sc.nextLine();
			
			// *표 입력시 메인메뉴로 돌아간다
			keyReturnMain(userid, sc); 
			
			if(isUsableUserId(userid)) {
				System.out.println("~~~ 등록된 회원ID가 아닙니다 ~~~\n");
			}
			else {
				break;
			}
		} while(true);
		
		
		File file = new File(RENTALTASKLIST_FILE); // 대여업무 파일
		List<RentalTaskDTO> rentalTaskList = null;
		
		if(!file.exists()) { // 대여업무 파일이 존재하지 않는 경우. 최초로 대여입력시 새로운 리스트를 만든다
			rentalTaskList = new ArrayList<>();
		}
		else { // 대여업무 파일이 존재하는 경우. 두번째 이후로 대여입력시 파일을 리스트형태로 변환한다
			Object rentalTaskListObj = serial.getObjectFromFile(RENTALTASKLIST_FILE); 
			rentalTaskList = (List<RentalTaskDTO>) rentalTaskListObj;
			
			// === 도서를 대여해가려는 회원의 미반납된 도서중 반납예정일이 지난 미반납된 도서가 있는지 알아봐서 있다면 대여를 불가하도록 만든다. ===
			for(RentalTaskDTO rt : rentalTaskList) {
				if(rt.getUserid().equals(userid)) {
					try {
						SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy-MM-dd"); 
						String currentDay = sdfmt.format(new Date());
					
						Date currentDate = sdfmt.parse(currentDay);
					//	Date currentDate = sdfmt.parse("2020-08-15");
						Date scheduledReturnDate = sdfmt.parse(rt.getScheduledReturnDay());
						
						long diff = currentDate.getTime() - scheduledReturnDate.getTime();
						
						if(diff > 0) {
							System.out.println("~~~~~ 반납예정일을 넘긴 미반납된 도서가 존재하므로 도서대여가 불가능합니다.!!!");
							return; // 메소드 종료
						}
						
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
				}
			}// end of for------------------------------------------
			
		}// end of if~else------------------------------------------
		
		// === 개별도서 객체에 대여가능여부를 알아본후 true(비치중)을 false(대여중)로 변경하기 위해 bookIdList 를 불러옴 === 
		Object bookIdListObj = serial.getObjectFromFile(BOOKIDLIST_FILE); 
		List<BookIdDTO> bookIdList = (List<BookIdDTO>) bookIdListObj;
		
		// 대여권수만큼 반복문을 돌리기 위한 nTotalCount
		int nTotalCount = 0;
		do {
			System.out.print("▶ 총대여권수: ");
			String sTotalCount = sc.nextLine();
			
			// *표 입력시 메인메뉴로 돌아간다
			keyReturnMain(sTotalCount, sc); 

			try {
				nTotalCount = Integer.parseInt(sTotalCount);
				if(nTotalCount < 1) {
					System.out.println("~~~ 총대여권수는 1 이상이어야 합니다. ~~~\n");
					continue;
				}
				else {
					break;
				}
			} catch(NumberFormatException e) {
				System.out.println("~~~ 숫자로만 입력하세요 ~~~\n");
			}
		} while(true);
		
		
		// === 대여해갈 도서ID 입력하기 === 
		for(int i=0; i<nTotalCount; i++) {
			
			String sc_bookid = "";
			String r_bookid = "";
			
			do {
				System.out.print("▶ 도서ID: ");
				sc_bookid = sc.nextLine();

				r_bookid = removeHypen(sc_bookid);
				
				// *표 입력시 메인메뉴로 돌아간다
				keyReturnMain(r_bookid, sc); 

				
				if(isUsableBookId(r_bookid)) {
					System.out.println("~~~ 존재하지 않는 도서ID 입니다. 다시 입력하세요!! ~~~\n");
				}
				else {
					break;
				}
			} while(true);
		
			// === 대여중(rentalAvailable 값이 false)인 도서ID를 입력하면 대여가 불가하도록 만든다. 시작 ===
			boolean flag = false;
			for(BookIdDTO rtbook : bookIdList) {
				// 해당 도서가 있지만 대여중인경우
				if(r_bookid.equals(rtbook.getBookid()) && !rtbook.isRentalAvailable()) {
					System.out.println("~~~ 현재 대여중인 도서ID 입니다. 새로운 도서ID를 입력하세요!!");
					// flag를 true로 바꾼다
					flag = true;
					break;
				}
			}
			if(flag) {
				// 총 대여권수 +1 되었던 것을 다시 -1 한 뒤 for 문을 다시 시작한다
				i--;
				continue;
			} // === 대여중(rentalAvailable 값이 false)인 도서ID를 입력하면 대여가 불가하도록 만든다. 끝 ===
			
			
			RentalTaskDTO rt = new RentalTaskDTO();
			rt.setUserid(userid);
			rt.setBookid(r_bookid);
			rt.setRentalDay();
			rt.setScheduledReturnDay();
			
			rentalTaskList.add(rt);
			
			// === 개별도서 객체에 대여가능여부를 false(대여중)로 변경하기. 시작 === //
			for(int j=0; j<bookIdList.size(); j++) {
				if( r_bookid.equals(bookIdList.get(j).getBookid()) ) {
					bookIdList.get(j).setRentalAvailable(false);
					break;
				}
			} // === 개별도서 객체에 대여가능여부를 false(대여중)로 변경하기. 끝 === //
			
			
		}// end of for---------------------------------------
		
		int n = serial.objectToFileSave(rentalTaskList, RENTALTASKLIST_FILE); 	
				
		if(n==1) {
			System.out.println(">>> 대여등록 성공!! <<<");
			int m = serial.objectToFileSave(bookIdList, BOOKIDLIST_FILE); 
			if(m==1) {
				System.out.println(">>> 대여도서 비치중에서 대여중으로 변경함 <<<");
			}
		}	
		else
			System.out.println(">>> 대여등록 실패!! <<<");
		
	}// end of public void rental(Scanner sc)---------------------------  
	
	

	// ~~~~~~~~~~ 11. 사서는 대여중인 도서에 대해 모든 정보를 조회할 수 있도록 한다. ~~~~~~~~~~
	@SuppressWarnings("unchecked")
	@Override
	public void rentAllView() {  // 사서는 대여중인 도서에 대해 모든 정보를 조회할 수 있도록 한다.
	
		Object rentalTaskListObj = serial.getObjectFromFile(RENTALTASKLIST_FILE); 
		List<RentalTaskDTO> rentalTaskList = (List<RentalTaskDTO>) rentalTaskListObj;
		
		Object userMapObj = serial.getObjectFromFile(USERLIST_FILE); 
		Map<String, UserDTO> userMap = (HashMap<String, UserDTO>) userMapObj;
		
		Object bookInfoMapObj = serial.getObjectFromFile(BOOKINFOLIST_FILE); 
		Map<String, BookInfoDTO> bookInfoMap = (HashMap<String, BookInfoDTO>) bookInfoMapObj;
		
		if(rentalTaskList != null) {
			System.out.println("============================================================================================================================");
			System.out.println("도서ID              ISBN                도서명           작가명     출판사      회원ID      회원명     연락처        대여일자       반납예정일 ");
			System.out.println("============================================================================================================================");
			
			for(RentalTaskDTO rt: rentalTaskList) {
				
				UserDTO rtuser = userMap.get(rt.getUserid()); // 대여해간 회원정보 알아오기 
				
				String isbn = rt.getBookid().substring(0, 13);
				BookInfoDTO bookinfo = bookInfoMap.get(isbn); // 대여해간 도서정보 알아오기 
				
				rt.setRtuser(rtuser);     // 대여업무 객체에 회원정보 입력하기
				rt.setBookinfo(bookinfo); // 대여업무 객체에 도서정보 입력하기
				
				System.out.println(rt);   // 대여업무 출력
			}// end of for--------------------------------------------------------------
		}
		
		else {
			System.out.println("~~~ 대여정보가 없습니다  ~~~\n");
		}
	}// end of public void rentAllView()-----------------------------------
	
	
	
	// 16자리의 숫자 r_bookid 를 123-45-67890-12-3-001 의 형태로 변경
	@Override
	public String addHypenBookId(String r_bookid) {

		String a_bookid = r_bookid.replaceFirst("(^|[0-9]{3})([0-9]{2})([0-9]{5})([0-9]{2})([0-9]{1})([0-9]{3})$",
				"$1-$2-$3-$4-$5-$6");

		return a_bookid;

	}
	
	
	// ~~~~~~~~~~ 13. 대여자가 도서를 반납하기 위해 사서에게 도서를 가져다 주면 사서는 도서를 반납처리를 하는데 연체가 있을시 연체 1일당 100원을 연체료로 부과되어 표시되도록 한다. ~~~~~~~~~~ 
	//                반납된 도서는 대여목록에서 제거하고, 개별도서목록에서 비치중으로 변경한다.
	@SuppressWarnings("unchecked")
	@Override
	public void bookReturn(Scanner sc) { // 도서반납하기 
		
		Object rentalTaskListObj = serial.getObjectFromFile(RENTALTASKLIST_FILE); 
		List<RentalTaskDTO> rentalTaskList = (List<RentalTaskDTO>) rentalTaskListObj;
		
		Object bookIdListObj = serial.getObjectFromFile(BOOKIDLIST_FILE); 
		List<BookIdDTO> bookIdList = (List<BookIdDTO>) bookIdListObj;
		
		System.out.println("\n >>> 도서반납하기 <<<" + RESET_MSG);
		
		int nTotalCount = 0;
		do {
			System.out.print("▶ 총반납권수: ");
			String sTotalCount = sc.nextLine();
			
			// *표 입력시 메인메뉴로 돌아간다
			keyReturnMain(sTotalCount, sc); 

			try {
				nTotalCount = Integer.parseInt(sTotalCount);
				if(nTotalCount < 1) {
					System.out.println("~~~ 총반납권수는 1 이상이어야 합니다. ~~~\n");
					continue;
				}
				else {
					break;
				}
			} catch(NumberFormatException e) {
				System.out.println("~~~ 숫자로만 입력하세요 ~~~\n");
			}
		} while(true);
		
		
		// === 반납하는 도서ID 입력하기 === 
		int sumArrears = 0; // 연체료 합계 변수 
		for(int i=0; i<nTotalCount; i++) {
			
			String sc_bookid = "";
			String r_bookid = "";
			
			do {
				System.out.print("▶ 반납도서ID: ");
				
				sc_bookid = sc.nextLine();

				// *표 입력시 메인메뉴로 돌아간다
				keyReturnMain(sc_bookid, sc); 

				r_bookid = removeHypen(sc_bookid);
				
				if(isUsableBookId(r_bookid)) {
					System.out.println("~~~ 존재하지 않는 도서ID 입니다. 다시 입력하세요!! ~~~\n");
				}
				else {
					break;
				}
			} while(true);
			
			
			// === 대여목록에서 삭제하기. 시작 === //
			for(int j=0; j<rentalTaskList.size(); j++) {
				if( r_bookid.equals(rentalTaskList.get(j).getBookid()) ) {
					
					// === 해당도서에 대한 연체료를 보여준다. 시작 === //
					rentalTaskList.get(j).setArrears();
					System.out.println("도서별 연체료: "+rentalTaskList.get(j).sArrears());
					sumArrears += rentalTaskList.get(j).getArrears();
					// === 해당도서에 대한 연체료를 보여준다. 끝 === //
					
					rentalTaskList.remove(j); // 대여목록에서 삭제하기
					break;
				}
			} // === 대여목록에서 삭제하기. 끝 === //
			
			
			// === 개별도서 객체에 대여가능여부를 true(비치중)로 변경하기. 시작 === //
			for(int j=0; j<bookIdList.size(); j++) {
				if( r_bookid.equals(bookIdList.get(j).getBookid()) ) {
					bookIdList.get(j).setRentalAvailable(true);
					break;
				}
			} // === 개별도서 객체에 대여가능여부를 false(비치중)로 변경하기. 끝 === //
						
		}// end of for---------------------------------------
		
		
		int n = serial.objectToFileSave(rentalTaskList, RENTALTASKLIST_FILE); 	
		
		if(n==1) {
			System.out.println(">>> 도서반납 성공!! <<<");
			int m = serial.objectToFileSave(bookIdList, BOOKIDLIST_FILE); 
			if(m==1) {
				System.out.println(">>> 대여도서 대여중에서 비치중으로 변경함 <<<");
			}
			
			DecimalFormat df = new DecimalFormat("#,###");
			String sSumArrears = (String)df.format(sumArrears);
			System.out.println("▶ 연체료 총계: " + sSumArrears+"원");
		}	
		else
			System.out.println(">>> 도서반납 실패!! <<<");
		
	}// end of public void bookReturn(Scanner sc)------------------------------
	
	
	
	// ~~~~~~~~~~ 2. 일반회원 전용 메뉴를 만든다. ~~~~~~~~~~
	// == 일반회원 전용메뉴 == 
	@Override
	public void rentalUserMenu(Scanner sc) {
		
		UserDTO rtuser = null;
		
		String sMenuno = "";
		
		do {
			String loginName = (rtuser==null)?"":"["+rtuser.getName()+" 로그인중..]";
			
			String sMenu = "\n>>>> 일반회원 전용 Menu "+loginName+" <<<<\n" + 
				      "1.일반회원가입    2.로그인    3.로그아웃   4.도서검색하기    5.나의대여현황보기    6.나가기\n"+ 
	                  "=> 메뉴번호선택 : ";
		
			System.out.print(sMenu);
		
			sMenuno = sc.nextLine(); // 사서전용 Menu 메뉴번호 선택하기
		
			switch (sMenuno) {
				case "1":  // 일반회원가입
					registerRentalUser(sc);
					break;
					
				case "2":  // 로그인
					rtuser = loginRentalUser(sc);
					break;
					
				case "3":  // 로그아웃
					rtuser = null;
					break;
					
				case "4":  // 도서검색하기
					if(rtuser != null) {
						searchBook(sc);
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}

					break;
					
				case "5":  // 나의대여현황보기
					if(rtuser != null) {
						rentMyView(rtuser.getUserid());
					}
					else {
						System.out.println(">> 먼저 로그인 하셔야 합니다. <<");
					}

					break;	
					
				case "6":  // 나가기
					
					break;		
					
				
				default:
					System.out.println("~~~~ 존재하지 않는 메뉴번호 입니다.!!");
					break;
			}	
			
		} while (!"6".equals(sMenuno));	
		
	}// end of public void rentalUserMenu(Scanner sc)----------------------------------------
	

	// ~~~~~~~~~~ 7. 일반회원 객체(Map)를 파일에 저장한다. ~~~~~~~~~~ 
	// == 7.1 일반회원가입(Map) ==
	@SuppressWarnings("unchecked")
	@Override
	public void registerRentalUser(Scanner sc) {
		
		System.out.println("\n== 일반회원 가입하기 ==" + RESET_MSG);
		String userid = "";
		
		// 일반회원 ID 중복검사하기 
		do {
			System.out.print("▶ 회원ID: ");
			userid = sc.nextLine();  // leess , eomjh
			
			// *표 입력시 메인메뉴로 돌아간다
			keyReturnMain(userid, sc); 

			if(userid==null || userid.trim().isEmpty()) {
				System.out.println("~~~ 회원아이디를 입력하세요!!");
				continue;
			}
			else if(!isUsableUserId(userid)) {
				System.out.println("~~~ "+userid+" 는 이미 존재하므로 다른 회원ID를 입력하세요!!");
				continue;
			}
			else {
				break;
			}
		} while (true);
				
		System.out.print("▶ 암호: ");  // 1234 , 1234
		String pwd = sc.nextLine();
		
		System.out.print("▶ 성명: ");  // 이순신, 엄정화
		String name = sc.nextLine();
		
		System.out.print("▶ 연락처: ");  // 010-2345-6789 , 010-9876-5432
		String phone = sc.nextLine();
				
		UserDTO rtuser = new UserDTO(userid, pwd, name, phone);
				
		File file = new File(USERLIST_FILE); // USERLIST_FILE 에 해당하는 파일객체 생성하기
		Map<String, UserDTO> rentalUserMap = null;
		int n = 0;
		
		if(!file.exists()) { // 파일이 존재하지 않는 경우. 즉, 최초로 부서등록을 하는 경우이다.
			rentalUserMap = new HashMap<>(); 
			rentalUserMap.put(userid, rtuser);
			n = serial.objectToFileSave(rentalUserMap, USERLIST_FILE); // USERLIST_FILE 파일에 rentalUserMap 객체를 저장시킨다.	
		}
		else { // 파일이 존재하는 경우. 두번째 이후로 부서가입시
			Object rentalUserMapObj = serial.getObjectFromFile(USERLIST_FILE); // USERLIST_FILE 파일에 저장된 객체를 불러온다.
			rentalUserMap = (HashMap<String, UserDTO>) rentalUserMapObj;
			rentalUserMap.put(userid, rtuser);
			n = serial.objectToFileSave(rentalUserMap, USERLIST_FILE); // USERLIST_FILE 파일에 rentalUserMap 객체를 저장시킨다.
		}
		
		if(n==1)
			System.out.println(">>> 회원등록 성공!! <<<");
		else
			System.out.println(">>> 회원등록 실패!! <<<");
		
	}// end of public void registerRentalUser(Scanner sc)-----------------------------------------

	
	// == 7.2 일반회원 ID 중복검사하기  ==
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUsableUserId(String userid) {
		
		boolean isUse = true; // 최초로 가입시 파일에 저장된 rentalUserMapObj 객체가 없기애 중복된 회원아이디가 없으므로 사용가능하도록 한다.
		
		Object rentalUserMapObj = serial.getObjectFromFile(USERLIST_FILE); 
		
		if(rentalUserMapObj != null) { // 파일에 저장된 rentalUserMapObj 객체가 있다라면 
			HashMap<String, UserDTO> rentalUserMap = (HashMap<String, UserDTO>)rentalUserMapObj;
			UserDTO rtuser = rentalUserMap.get(userid);
			if(rtuser != null) { // 회원아이디가 존재하는 경우이라면 
				isUse = false;
			}
		}
		
		return isUse;
	}// end of public boolean isUseUserID(String userid)-----------------------------------	
	
		
	// ~~~~~~~~~~ 8. 회원으로 로그인 하기 ~~~~~~~~~~ 
	@SuppressWarnings("unchecked")
	@Override
	public UserDTO loginRentalUser(Scanner sc) { // 일반회원으로 로그인 하기
	
		UserDTO rtuser = null;
		
		System.out.println("\n == 로그인 하기 == " + RESET_MSG); 
		
		System.out.print("▶ 회원아이디: ");
		String userid = sc.nextLine();
		
		// *표 입력시 메인메뉴로 돌아간다
		keyReturnMain(userid, sc); 

		
		System.out.print("▶ 암호: ");
		String pwd = sc.nextLine();
		
		Object rentalUserMapObj = serial.getObjectFromFile(USERLIST_FILE); 
		
		if(rentalUserMapObj != null) { // 파일로 부터 객체정보를 얻어온 경우이라면   
			HashMap<String, UserDTO> rentalUserMap = (HashMap<String, UserDTO>) rentalUserMapObj;	
			
			rtuser = rentalUserMap.get(userid);
			
			if(rtuser != null && rtuser.getUserid().equals(userid) && rtuser.getPwd().equals(pwd)) {
				System.out.println(">>> 로그인 성공!!! <<<");
				return rtuser;
			}
			
			else {
				System.out.println(">>> 로그인 실패!!! <<<");
				return null;	
			}
		}
		
		return rtuser;
		
	}// end of public RentalUser loginRentalUser(Scanner sc)------------------------------------ 	
	
	
	// ~~~~~~~~~~ 9. 도서 검색하기  ~~~~~~~~~~
	// 대여자는 아래에 제시된 정보를 토대로 도서정보(ISBN, 도서아이디, 도서명, 작가명, 출판사, 가격, 대여상태)를 검색할 수 있도록 한다.
	// (1) 도서분류카테고리 입력 (입력치 않고 엔터를 하면 검색대상에서 제외토록 한다.)
	// (2) 도서명 입력 (입력치 않고 엔터를 하면 검색대상에서 제외토록 한다.)
	// (3) 작가명 입력 (입력치 않고 엔터를 하면 검색대상에서 제외토록 한다.)
	// (4) 출판사명 입력 (입력치 않고 엔터를 하면 검색대상에서 제외토록 한다.)
	
	@SuppressWarnings("unchecked")
	@Override // 도서 검색하기 
	public void searchBook(Scanner sc) { 
		
		System.out.println("\n >>> 도서검색하기 <<<"); 
		
		System.out.println("[주의사항] 검색어를 입력치 않고 엔터를 하면 검색대상에서 제외됩니다.");
		
		System.out.print("▶ 도서분류카테고리(Programming , DataBase 등): ");
		String category = sc.nextLine();
		if(category.trim().isEmpty()) {
			category = "";
		}
		
		System.out.print("▶ 도서명: ");
		String bookname = sc.nextLine();
		if(bookname.trim().isEmpty()) {
			bookname = "";
		}
		
		System.out.print("▶ 작가명: ");
		String authorname = sc.nextLine();
		if(authorname.trim().isEmpty()) {
			authorname = "";
		}
		
		System.out.print("▶ 출판사명: ");
		String publisher = sc.nextLine();
		if(publisher.trim().isEmpty()) {
			publisher = "";
		}
		
		Object bookIdListObj = serial.getObjectFromFile(BOOKIDLIST_FILE); 
		
		if(bookIdListObj == null) {
			System.out.println("~~~~ 비치된 도서가 없습니다. ~~~~");
		}
		else {
			System.out.println("==================================================================================================");
			System.out.println("ISBN                 도서아이디                도서명           작가명     출판사      가격      대여상태");
			System.out.println("==================================================================================================");
			List<BookIdDTO> bookIdList = (List<BookIdDTO>) bookIdListObj;
			
			boolean flag = false;
			for(BookIdDTO bookIdDto : bookIdList) {
				
				BookInfoDTO bookinfo = bookIdDto.getBookInfo();
				boolean b_category = bookinfo.getCategory().toLowerCase().contains(category.toLowerCase());
				boolean b_bookname = bookinfo.getBookname().toLowerCase().contains(bookname.toLowerCase());
				boolean b_authorname = bookinfo.getAuthorname().toLowerCase().contains(authorname.toLowerCase());
				boolean b_publisher = bookinfo.getPublisher().toLowerCase().contains(publisher.toLowerCase());
				
				if(b_category && b_bookname && b_authorname && b_publisher) {
					flag = true;
					System.out.println(bookIdDto);
				}
				
			}// end of for---------------------------
			
			if(!flag) {
				System.out.println("~~~~ 검색에 일치하는 도서가 없습니다. ~~~~");
			}
		}
		
	}// end of public void searchBook(Scanner sc)------------------------------------------------
	
	
	// ~~~~~~~~~~ 12. 대여자는 자신이 대여한 도서대여 정보를 조회할 수 있도록 한다. ~~~~~~~~~~
	@SuppressWarnings("unchecked")
	@Override
	public void rentMyView(String userid) {  // 대여자는 자신이 대여한 도서대여 정보를 조회할 수 있도록 한다.
	
		Object rentalTaskListObj = serial.getObjectFromFile(RENTALTASKLIST_FILE); 
		List<RentalTaskDTO> rentalTaskList = (List<RentalTaskDTO>) rentalTaskListObj;
		
		Object bookInfoMapObj = serial.getObjectFromFile(BOOKINFOLIST_FILE); 
		Map<String, BookInfoDTO> bookInfoMap = (HashMap<String, BookInfoDTO>) bookInfoMapObj;
		
		if(rentalTaskList != null) {
			System.out.println("===========================================================================================================");
			System.out.println("도서ID              ISBN                   도서명        작가명     출판사      회원ID     대여일자          반납예정일 ");
			System.out.println("===========================================================================================================");
			
			boolean flag = false;
			for(RentalTaskDTO rt: rentalTaskList) {
				
				if(userid.equals(rt.getUserid())) {
				    flag = true; 
					
					String isbn = rt.getBookid().substring(0, 13);
					BookInfoDTO bookinfo = bookInfoMap.get(isbn); // 대여해간 도서정보 알아오기 
					
					rt.setBookinfo(bookinfo); // 대여업무 객체에 도서정보 입력하기
					
					System.out.println(addHypenBookId(rt.getBookid())+ "   " +
					                   addHypenISBN(rt.getBookinfo().getIsbn())+ "   " +
							           rt.getBookinfo().getBookname()+ "   " +
					                   rt.getBookinfo().getAuthorname()+ "   " + 
					                   rt.getBookinfo().getPublisher()+ "   " + 
					                   userid+ "   " +
					                   rt.getRentalDay()+ "   " +
					                   rt.getScheduledReturnDay() );   // 대여업무 출력
				}
				
			}// end of for--------------------------------------------------------------
			
			if(!flag) {
				System.out.println("~~~ 대여해가신 도서가 없습니다.  ~~~\n");
			}
		}
		
		else {
			System.out.println("~~~ 대여정보가 없습니다  ~~~\n");
		}
	}// end of public void rentMyView()-----------------------------------
	
	
	// *표 입력시 main으로 돌아가게 하는 메서드
	public void keyReturnMain(String sc_key, Scanner sc) {
		if (sc_key.equals(RESET_KEY)) { // *을 입력한 경우 메인메뉴로 돌아간다.
			Return_Main(sc);
		}
	}
	
	
}
