package com.mapfinger.entity;

import java.util.ArrayList;
import java.util.List;
import com.mapfinger.entity.location.Location;

public class LofNode {
	private int id;
	private Location location;
	private double kDistance;
	private List<LofNode> kNeighbors;
	private double distance;
	private double reachableDistance;
	private double reachableDensity;
	private double lof;
	
	public LofNode() {
		this.kNeighbors = new ArrayList<LofNode>();
	}
	
	public LofNode(int id, Location location) {
		this();
		this.id = id;
		this.location = location;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public double getkDistance() {
		return kDistance;
	}
	
	public void setkDistance(double kDistance) {
		this.kDistance = kDistance;
	}
	
	public List<LofNode> getkNeighbors() {
		return kNeighbors;
	}
	
	public void setkNeighbors(List<LofNode> kNeighbors) {
		this.kNeighbors = kNeighbors;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getReachableDistance() {
		return reachableDistance;
	}
	
	public void setReachableDistance(double reachableDistance) {
		this.reachableDistance = reachableDistance;
	}
	
	public double getReachableDensity() {
		return reachableDensity;
	}
	
	public void setReachableDensity(double reachableDensity) {
		this.reachableDensity = reachableDensity;
	}
	
	public double getLof() {
		return lof;
	}
	
	public void setLof(double lof) {
		this.lof = lof;
	}
	
}
