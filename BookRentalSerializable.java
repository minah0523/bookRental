package project.bookrental.management;

import java.io.*;

public class BookRentalSerializable {

	// 직렬화하는 메소드 생성하기 (메모리상에 올라온 객체를 하드디스크 파일에 저장시키기) 
	public int objectToFileSave(Object obj, String saveFilename) {  
		
		// === 객체 obj 를 파일 saveFilename 으로 저장하기 === //
		try {
			FileOutputStream fost = new FileOutputStream(saveFilename, false); 
			/*
			       만약에  탐색기에서 
			    saveFilename 파일이 없다라면 파일을 자동으로 생성해준다.
			       단, 탐색기에서 saveFilename 에 해당하는 폴더는 존재해야 한다.
			    
			       두번째 파라미터인 append 가 true 인 경우는
			       첫번째 파라미터인 해당파일에 이미 내용물이 적혀 있는 경우일때 기존내용물은 그대로 두고 
			       기존내용뒤에 새로운 내용을 덧붙여 추가하겠다는 말이다. 
			       
			       두번째 파라미터인 append 가 false 인 경우는
			       첫번째 파라미터인 해당파일에 이미 내용물이 적혀 있는 경우일때 기존내용물은 싹 지우고 
			       새로운 내용을 새롭게 처음부터 쓰겠다는 말이다.
			     
			       그런데 만약에 두번째 파라미터를 생략하면    
			       즉, FileOutputStream fost = new FileOutputStream(filename); 이라면
			       자동적으로 false 로 인식한다.   
			*/
			// 출력 노드스트림(빨대꽂기)
			// 파일 이름(saveFilename)을 이용해서 FileOutputStream 객체를 생성한다. 
			// 생성된 객체는 두번째 파라미터 boolean append 값에 따라 true 이면 기존 파일에 내용을 덧붙여 추가할 것이고, 
			// boolean append가 false 이면 기존 내용은 삭제하고 새로운 내용이 기록되도록 하는 것이다.
			
			BufferedOutputStream bufOst = new BufferedOutputStream(fost, 1024);  
			// 필터스트림(노드스트림에 오리발장착하기)
			
			ObjectOutputStream objOst = new ObjectOutputStream(bufOst);
			// 객체 obj 를 파일 saveFilename 에 기록하는(저장하는) 스트림 생성하기 
			
			objOst.writeObject(obj);
			// ObjectOutputStream objOst 을 사용하여 객체 obj 를 파일 saveFilename 에 기록하기(저장하기)
			
			objOst.close(); // 사용된 자원반납하기(사용된 객체를 메모리공간에서 삭제하기) 
			bufOst.close(); // 사용된 자원반납하기(사용된 객체를 메모리공간에서 삭제하기)
			fost.close();   // 사용된 자원반납하기(사용된 객체를 메모리공간에서 삭제하기)
			
			return 1;
			
		} catch (IOException e) {
			// e.printStackTrace();
			return 0;
		} 
		
	}
	
	
	// 역직렬화하는 메소드 생성하기 (하드디스크에 저장된 파일을 읽어다가 객체로 만들어 메모리에 올리게 하는것) 
	public Object getObjectFromFile(String fileName) { 
	  			
		// === 파일 filename 을 읽어서 객체 obj 로 변환하기 === //
		
		try {
			FileInputStream finst = new FileInputStream(fileName); 
			// 입력 노드스트림(빨대꽂기)
			
			BufferedInputStream bufInst = new BufferedInputStream(finst, 1024);
			// 필터스트림(노드스트림에 오리발장착하기)
			
			ObjectInputStream objInst = new ObjectInputStream(bufInst); 
			// 파일 filename 을 읽어서 객체로 만들어주는 스트림 생성하기
			
			Object obj = objInst.readObject();
			// ObjectInputStream objInst 을 사용하여 파일 filename 에 기록(저장)되어졌던것을 객체로 만들기 
			
			objInst.close(); // 사용된 자원반납하기(사용된 객체를 메모리공간에서 삭제하기) 
			bufInst.close(); // 사용된 자원반납하기(사용된 객체를 메모리공간에서 삭제하기) 
			bufInst.close(); // 사용된 자원반납하기(사용된 객체를 메모리공간에서 삭제하기) 
			
			return obj;
			
		} catch(IOException | ClassNotFoundException e) {
			// e.printStackTrace();
			return null;
		}
		
		
	}
	
}
