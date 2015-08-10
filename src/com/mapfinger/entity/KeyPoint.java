package com.mapfinger.entity;

public class KeyPoint extends Coord {
	private static final long serialVersionUID = 3411259228183908891L;
	
	private String arriveTime;
	private String leaveTime;
	private String address;
	
	public KeyPoint() {
		super();
	}
	
	public KeyPoint(String longitude, String latitude, String arriveTime, String leaveTime,
			String address) {
		super(longitude, latitude);
		this.arriveTime = arriveTime;
		this.leaveTime = leaveTime;
		this.address = address;
	}
	
	public String getArriveTime() {
		return arriveTime;
	}
	
	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}
	
	public String getLeaveTime() {
		return leaveTime;
	}
	
	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
		return "KeyPoint [longitude=" + getLongitude() + ", latitude=" + getLatitude() + ", arriveTime="
				+ arriveTime + ", leaveTime=" + leaveTime + ", address=" + address + "]";
	}
	
}
