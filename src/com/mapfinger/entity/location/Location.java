package com.mapfinger.entity.location;

public class Location extends Coord {
	private static final long serialVersionUID = -877673203139865410L;
	
	private String timeline;
	private String address;
	
	public Location() {
		super();
	}
	
	public Location(String timeline, String longitude, String latitude, String address) {
		super(longitude, latitude);
		this.timeline = timeline;
		this.address = address;
	}
	
	public String getTimeline() {
		return timeline;
	}
	
	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
		return "Location [timeline=" + timeline + ", longitude=" + getLongitude() + ", latitude="
				+ getLatitude() + ", address=" + address + "]";
	}
	
}
