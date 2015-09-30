package com.mapfinger.entity.location;

public class Point extends Location {
	private static final long serialVersionUID = 8904834406310635967L;
	
	private double oslope;
	private double pslope;
	private double stay;
	private double distance;
	
	public Point() {
		super();
	}
	
	public Point(String timeline, String longitude, String latitude, String address, double oslope,
			double pslope, double stay, double distance) {
		super(timeline, longitude, latitude, address);
		this.oslope = oslope;
		this.pslope = pslope;
		this.stay = stay;
		this.distance = distance;
	}
	
	public double getOslope() {
		return oslope;
	}
	
	public void setOslope(double oslope) {
		this.oslope = oslope;
	}
	
	public double getPslope() {
		return pslope;
	}
	
	public void setPslope(double pslope) {
		this.pslope = pslope;
	}
	
	public double getStay() {
		return stay;
	}
	
	public void setStay(double stay) {
		this.stay = stay;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return "Point [timeline=" + getTimeline() + ", longitude=" + getLongitude() + ", latitude="
				+ getLatitude() + ", address=" + getAddress() + ", oslope=" + oslope + ", pslope="
				+ pslope + ", stay=" + stay + ", distance=" + distance + "]";
	}
}
