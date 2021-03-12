package project.bookrental.management;

import java.io.Serializable;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 7555311588401067006L;
	private String userid;     // 회원아이디
	private String pwd;        // 암호
	private String name;       // 성명
	private String phone;      // 연락처
	
	public UserDTO() {}
	
	public UserDTO(String userid, String pwd, String name, String phone) {
		this.userid = userid;
		this.pwd = pwd;
		this.name = name;
		this.phone = phone;
	}

	public String getUserid() {
		return userid;
	}

	public void setId(String userid) {
		this.userid = userid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
		
}
