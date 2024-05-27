package kr.item.vo;

import java.sql.Date;

public class ItemVO {
	private int item_num;		//상품번호
	private String name;		//상품명
	private int price;			//가격
	private int quantity;		//수량
	private String photo1;		//상품사진1
	private String photo2;		//상품사진2
	private String detail;		//설명
	private Date reg_date;		//등록일
	private Date modify_date;	//수정일
	private int status;			//상품 판매 가능 여부(1:미표시,2:표시)
	
	public int getItem_num() {
		return item_num;
	}
	public void setItem_num(int item_num) {
		this.item_num = item_num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getPhoto1() {
		return photo1;
	}
	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}
	public String getPhoto2() {
		return photo2;
	}
	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}





