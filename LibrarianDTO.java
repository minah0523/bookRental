package project.bookrental.management;

import java.io.Serializable;

public class LibrarianDTO implements Serializable{

	private static final long serialVersionUID = -6140277618621906813L;
	private String lbid;  // 사서아이디
	private String pwd;   // 암호
	
	public LibrarianDTO() {}
	
	public LibrarianDTO(String lbid, String pwd) {
		this.lbid = lbid;
		this.pwd = pwd;
	}

	public String getLbid() {
		return lbid;
	}

	public void setLbid(String lbid) {
		this.lbid = lbid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}
