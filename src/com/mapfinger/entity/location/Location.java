package com.mapfinger.entity.location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.mapfinger.io.MapfingerConfiguration;

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
	
	/**
	 * @param location
	 *            the previous location
	 * @return distance from this(current) location to previous location
	 */
	public double calDistanceTo(Location location) {
		double[] lat = new double[] { Double.parseDouble(this.getLatitude()),
				Double.parseDouble(location.getLatitude()) };
		double[] lon = new double[] { Double.parseDouble(this.getLongitude()),
				Double.parseDouble(location.getLongitude()) };
		
		double delta = Math.sin(lat[0]) * Math.sin(lat[1]) + Math.cos(lat[0]) * Math.cos(lat[1])
				* Math.cos(lon[0] - lon[1]);
		double dist = Math.PI * MapfingerConfiguration.getMapfingerConfiguration().getR() * Math.acos(delta)
				* 1000 / 180;
		
		return dist;
	}
	
	/**
	 * @param location
	 *            the previous location
	 * @return timeDiff from this(current) location to previous location
	 */
	public double calTimeDiffTo(Location location) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		double diff = 0.0;
		
		try {
			Date[] date = { format.parse(this.getTimeline()), format.parse(location.getTimeline()) };
			diff = Math.abs((date[0].getTime() - date[1].getTime()) / 1000 * 1.0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return diff;
	}
	
	/**
	 * @param location
	 *            the previous point
	 * @return slope from this(current) location to previous location
	 */
	public double calSlopeTo(Location location) {
		double slope = 361;
		if (!this.getLongitude().equals(location.getLongitude())) {
			slope = (Double.parseDouble(this.getLatitude()) - Double.parseDouble(location.getLatitude()))
					/ (Double.parseDouble(this.getLongitude()) - Double.parseDouble(location.getLongitude()))
					* 1.0;
		}
		
		return slope;
	}
	
	/**
	 * @param late
	 *            late second
	 * @return the late time of this(current) time
	 */
	public String calTimeLateOf(double late) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time;
		
		try {
			Date date = format.parse(this.getTimeline());
			time = format.format(date.getTime() + (long) (late * 1000));
		} catch (ParseException e) {
			time = null;
		}
		
		return time;
	}
}
