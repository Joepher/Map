package com.mapfinger.entity;

import java.io.Serializable;

public class Coord implements Serializable {
	private static final long serialVersionUID = -5847419207967228007L;
	
	private String longitude;
	private String latitude;
	
	public Coord() {}
	
	public Coord(String longitude, String latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	@Override
	public String toString() {
		return "Coord [longitude=" + longitude + ", latitude=" + latitude + "]";
	}
	
}
