package kr.member.vo;

import java.sql.Date;

public class MemberVO {
	private int mem_num; 		// 회원 번호
	private String id;			// 회원 아이디
	private int auth;			// 회원 등급
	
	private String name;		// 이름
	private String passwd;		// 비밀번호
	private String phone;		// 전화번호	
	private String email;		// 이메일
	private String zipcode;		// 우편번호
	private String address1;	// 주소
	private String address2;	// 상세 주소
	private String photo;		// 프로필 사진
	private Date reg_date;		// 가입일
	private Date modify_date;	// 수정일
	
	public boolean isCheckedPassword(String userPasswd) {
		// 회원 등급(auth) : 0: 탈퇴회원, 1: 정지회원, 2:일반회원, 9:관리자
		// 0, 1 은 로그인하지 못하게 조건을 설정해준다.
		if(auth>1 && passwd.equals(userPasswd)) {
			return true;
		}
		return false;
	}
	
	public int getMem_num() {
		return mem_num;
	}
	public void setMem_num(int mem_num) {
		this.mem_num = mem_num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAuth() {
		return auth;
	}
	public void setAuth(int auth) {
		this.auth = auth;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Date getReg_date() {
		return reg_date;
	}
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	public Date getModify_date() {
		return modify_date;
	}
	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}
	
}
