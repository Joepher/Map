package com.mapfinger.test;

import com.mapfinger.entity.location.Location;

public class LocationTest {
	
	static Location getDiffLoc(Location loc, double lonDiff, double latDiff) {
		Location location = new Location();
		
		location.setLongitude(String.valueOf(Double.valueOf(loc.getLongitude()) + lonDiff));
		location.setLatitude(String.valueOf(Double.valueOf(loc.getLatitude()) + latDiff));
		
		return location;
	}
	
	public static void main(String[] args) {
		Location loca = new Location();
		loca.setLongitude("113.407731");
		loca.setLatitude("23.058085");
		
		Location locb = getDiffLoc(loca, 0.01, 0.000001);
		
		locb.setLongitude("113.407840");
		locb.setLatitude("23.058089");
		
		System.out.println(loca.calDistanceTo(locb));
	}
	
}
