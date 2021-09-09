package com.cos.blogapp.util;

public class Script {
	
	public static String back(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('"+msg+"');");
		sb.append("history.back();");
		sb.append("</script>");
		
		return sb.toString();
		
	}
	
	public static String href(String path) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("location.href='"+path+"';");
		sb.append("</script>");
		
		return sb.toString();
	}

	public static String href(String path, String msg) { //함수명이 같아도 매개변수가 다르므로 가능
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('"+msg+"');");
		sb.append("location.href='"+path+"';");
		sb.append("</script>");
		
		return sb.toString();
	}
}
