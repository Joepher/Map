package com.mapfinger.entity;

public class UserData {
	private String userName;
	private String fileName;
	
	public UserData() {}
	
	public UserData(String userName, String fileName) {
		this.userName = userName;
		this.fileName = fileName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public String toString() {
		return "UserData [userName=" + userName + ", fileName=" + fileName + "]";
	}
	
}
