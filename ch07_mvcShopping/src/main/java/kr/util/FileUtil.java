package kr.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class FileUtil {
	//업로드 상대경로
	private static final String UPLOAD_PATH = "/upload";
	//파일 생성
	public static String createFile(
			   HttpServletRequest request,String param)
			     throws IllegalStateException,IOException,
	                                     ServletException{
		//업로드 절대경로
		String upload = 
		request.getServletContext().getRealPath(UPLOAD_PATH);
		//파일 정보 얻기
		Part part = request.getPart(param);
		//파일명 구하기
		String filename = part.getSubmittedFileName();
		if(!filename.isEmpty()) {
			//파일 중복 방지를 위해 임의의 값_원래 파일명 형식으로 변경
			filename = UUID.randomUUID()+"_"+filename;
			part.write(upload+"/"+filename);
		}
		return filename;
	}
	//파일 삭제
	public static void removeFile(
			               HttpServletRequest request,
			                             String filename) {
		if(filename!=null) {
			//업로드 절대경로
			String upload = 
			request.getServletContext().getRealPath(UPLOAD_PATH);
			File file = new File(upload+"/"+filename);
			if(file.exists()) file.delete();
		}
	}
}







