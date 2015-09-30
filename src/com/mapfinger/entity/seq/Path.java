package com.mapfinger.entity.seq;

import java.util.LinkedList;
import java.util.List;
import com.mapfinger.entity.location.Coord;

public class Path {
	private List<Coord> origenCoords;
	private List<Coord> pathCoords;
	
	private static double distThreshold = 20; // meter
	private static final double R = 6378.137; // kilometer
	
	public Path() {
		this.origenCoords = new LinkedList<Coord>();
		this.pathCoords = new LinkedList<Coord>();
	}
	
	public void addCoord(Coord coord) {
		this.origenCoords.add(coord);
	}
	
	public void addCoords(List<Coord> coords) {
		this.origenCoords.addAll(coords);
	}
	
	public List<Coord> getPath() {
		return pathCoords;
	}
	
	public void compute() {
		int total = origenCoords.size();
		
		if (total > 0) {
			int startPos = 0, endPos = 0;
			double dist = 0.0;
			
			while (startPos < total) {
				endPos = ++startPos;
				while (endPos < total) {
					dist = getDistance(origenCoords.get(startPos), origenCoords.get(endPos));
					
					if (dist > distThreshold) {
						pathCoords.add(computeMeanCoord(origenCoords.subList(startPos, endPos)));
						startPos = endPos;
						break;
					}
					
					++endPos;
				}
			}
		}
	}
	
	private double getDistance(Coord ca, Coord cb) {
		double[] lat = { Double.valueOf(ca.getLatitude()), Double.valueOf(cb.getLatitude()) };
		double[] lon = { Double.valueOf(ca.getLongitude()), Double.valueOf(cb.getLongitude()) };
		
		double delta = Math.sin(lat[0]) * Math.sin(lat[1]) + Math.cos(lat[0]) * Math.cos(lat[1])
				* Math.cos(lon[0] - lon[1]);
		double dist = Math.PI * R * Math.acos(delta) * 1000 / 180;
		
		return dist;
	}
	
	private Coord computeMeanCoord(List<Coord> list) {
		double latSum = 0.0, lonSum = 0.0;
		
		for (int i = 0; i < list.size(); i++) {
			latSum += Double.valueOf(list.get(i).getLatitude());
			lonSum += Double.valueOf(list.get(i).getLongitude());
		}
		
		Coord coord = new Coord();
		coord.setLatitude(String.valueOf(latSum / list.size()));
		coord.setLongitude(String.valueOf(lonSum / list.size()));
		
		return coord;
	}
}
