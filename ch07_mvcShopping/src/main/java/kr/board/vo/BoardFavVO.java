package kr.board.vo;

public class BoardFavVO {
	private int board_num;
	private int mem_num;
	
	public BoardFavVO() {}
	
	public BoardFavVO(int board_num, int mem_num) {
		this.board_num = board_num;
		this.mem_num = mem_num;
	}
	
	public int getBoard_num() {
		return board_num;
	}
	public void setBoard_num(int board_num) {
		this.board_num = board_num;
	}
	public int getMem_num() {
		return mem_num;
	}
	public void setMem_num(int mem_num) {
		this.mem_num = mem_num;
	}
	
}
