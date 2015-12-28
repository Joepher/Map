/**
 * @author Administrator
 * @date 2015-6-8
 * @name KeyPointDetectionExecutor
 * @version 1.0
 * @description The core function is detecting keypoints from the oroginal locations.
 *              In keyPointDetection, algorithm that proposed by MSRA is used to detect key points. Theory
 *              that a
 *              geographic region where a user stayed over a certain time interval is considered in this
 *              algorithm.
 *              Certain amount of points will be regarded as key points by this theory. Thus may cause
 *              stituations as
 *              followed:
 *              key point that consist only one location will be expanded by server amount of locations; key
 *              point that
 *              consist amounts of locations will be partition to two key points.
 *              To solve the problem caused by that algorithm, an enhanced algorithm is proposed. Key points
 *              consist of
 *              Stay points and Interest points. Two rules are used to detect those points.
 */
package com.mapfinger.executor.keypoint;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.mapfinger.entity.LofNode;
import com.mapfinger.entity.UserData;
import com.mapfinger.entity.location.KeyPoint;
import com.mapfinger.entity.location.Location;
import com.mapfinger.executor.BaseExecutor;
import com.mapfinger.executor.data.LocationDataExtractExecutor;
import com.mapfinger.io.FileOperator;
import com.mapfinger.io.MapfingerConfiguration;

public class KeyPointDetectExecutor extends BaseExecutor {
	private String convDataPath, keyDataPath;
	private Thread keyThread;
	private boolean detectFinished;
	private List<Location> locations;
	private MapfingerConfiguration configuration;
	
	public KeyPointDetectExecutor(UserData userData) {
		super(userData);
		String home = FileOperator.getUserHome(userData.getUserName());
		this.convDataPath = home + configuration.getConvDataPath() + userData.getFileName();
		this.keyDataPath = home + configuration.getKeyDataPath() + userData.getFileName();
		this.detectFinished = false;
		this.locations = null;
		
		Runnable target = new Runnable() {
			@Override
			public void run() {
				runwork();
			}
		};
		this.keyThread = new Thread(target, "KeyPointDetectExecutor-keyThread");
	}
	
	@Override
	public boolean execute() {
		this.locations = new LocationDataExtractExecutor(null).extractConvLocationData(convDataPath);
		this.keyThread.start();
		
		while (!detectFinished) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected void runwork() {
		if (persistance(detectKeyPoint(locations), keyDataPath)) {
			logger.info("Succeed in saving key points to " + keyDataPath);
		}
		detectFinished = true;
	}
	
	private List<KeyPoint> detectKeyPoint(List<Location> locations) {
		if (locations == null) {
			return null;
		} else {
			List<KeyPoint> stayPoints = stayPointDetectionRule(locations);
			List<KeyPoint> interestPoints = interestPointDetectionRule(locations);
			
			List<KeyPoint> keyPoints = new ArrayList<KeyPoint>();
			keyPoints.addAll(stayPoints);
			keyPoints.addAll(interestPoints);
			
			logger.info("Detected " + keyPoints.size() + " key points (enhanced)");
			
			return keyPoints;
		}
	}
	
	private boolean persistance(List<KeyPoint> keyPoints, String filePath) {
		if (keyPoints == null) {
			return false;
		} else {
			try {
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath));
				for (KeyPoint keyPoint : keyPoints) {
					writer.write(keyPoint.getLatitude() + "," + keyPoint.getLongitude() + ","
							+ keyPoint.getArriveTime() + "," + keyPoint.getLeaveTime() + ","
							+ keyPoint.getAddress() + "\r");
					writer.flush();
				}
				writer.close();
			} catch (Exception e) {
				logger.error("Failed in saving " + filePath + ". ErrRef:" + e.getMessage());
				return false;
			}
			return true;
		}
	}
	
	private List<KeyPoint> stayPointDetectionRule(List<Location> locations) {
		List<KeyPoint> keyPoints = new ArrayList<KeyPoint>();
		
		List<LofNode> lofNodes = new ArrayList<LofNode>(locations.size());
		for (int i = 0; i < locations.size(); ++i) {
			lofNodes.add(new LofNode(i, locations.get(i)));
		}
		
		List<LofNode> outliers = new OutlierNodeDetect().getOutlierNodes(lofNodes);
		for (int i = 0; i < outliers.size(); ++i) {
			keyPoints.add(calMeanCoord(outliers.get(i).getLocation(),
					locations.get(outliers.get(i).getId() + 1)));
		}
		
		return keyPoints;
	}
	
	private List<KeyPoint> interestPointDetectionRule(List<Location> locations) {
		List<KeyPoint> keyPoints = new ArrayList<KeyPoint>();
		List<Location> interestPoints = new ArrayList<Location>();
		int pointNum = locations.size(), i = 0, posA = 0, posB = 0, posC = 0, posD = 0;
		double slopeA2B = 0.0, slopeB2C = 0.0, slopeC2D = 0.0, rotateAB2BC = 0.0, rotateBC2CD = 0.0;
		
		while (i < pointNum) {
			if ((posD - posA) == 3) {
				Location locA = locations.get(posA);
				Location locB = locations.get(posB);
				Location locC = locations.get(posC);
				Location locD = locations.get(posD);
				
				slopeA2B = locB.calSlopeTo(locA);
				slopeB2C = locC.calSlopeTo(locB);
				
				rotateAB2BC = getSlopeRotate(slopeA2B, slopeB2C);
				
				if (Math.abs(rotateAB2BC) >= configuration.getSlopeRotateThreshold()) {
					interestPoints.add(locA);
					interestPoints.add(locB);
					
					slopeC2D = locD.calSlopeTo(locC);
					rotateBC2CD = getSlopeRotate(slopeB2C, slopeC2D);
					
					if (Math.abs(rotateBC2CD) < configuration.getSlopeRotateThreshold()) {
						KeyPoint keyPoint = computeMeanCoord(interestPoints);
						if (keyPoint != null) {
							keyPoints.add(keyPoint);
						}
						interestPoints = new ArrayList<Location>();
					}
					
					posA = posC;
					posB = posD;
					posC = posD = ++i;
				} else {
					KeyPoint keyPoint = computeMeanCoord(interestPoints);
					if (keyPoint != null) {
						keyPoints.add(keyPoint);
					}
					interestPoints = new ArrayList<Location>();
					
					posA = posB;
					posB = posC;
					posC = posD;
					posD = ++i;
				}
			} else {
				posA = posB;
				posB = posC;
				posC = posD;
				posD = ++i;
			}
		}
		
		return keyPoints;
	}
	
	private KeyPoint calMeanCoord(Location curloc, Location nextloc) {
		KeyPoint meanCoord = new KeyPoint();
		
		if (nextloc.calTimeDiffTo(curloc) <= configuration.getLofThreshold()) {
			meanCoord.setLongitude((Double.parseDouble(curloc.getLongitude()) + Double.parseDouble(nextloc
					.getLongitude())) / 2 * 1.0 + "");
			meanCoord.setLatitude((Double.parseDouble(curloc.getLatitude()) + Double.parseDouble(nextloc
					.getLatitude())) / 2 * 1.0 + "");
			meanCoord.setArriveTime(curloc.getTimeline());
			meanCoord.setLeaveTime(nextloc.getTimeline());
			meanCoord.setAddress("UnknownAddr");
		} else {
			meanCoord.setLongitude(curloc.getLongitude());
			meanCoord.setLatitude(curloc.getLatitude());
			meanCoord.setArriveTime(curloc.getTimeline());
			meanCoord.setLeaveTime(curloc.calTimeLateOf(configuration.getTimeThreshold()));
			meanCoord.setAddress("UnknownAddr");
		}
		
		return meanCoord;
	}
	
	private double getSlopeRotate(double preSlope, double curSlope) {
		return (curSlope - preSlope) / preSlope * 1.0;
	}
	
	private KeyPoint computeMeanCoord(List<Location> interests) {
		KeyPoint meanCoord = new KeyPoint();
		double latSum = 0.0, lonSum = 0.0;
		
		if (interests.size() >= 4) {
			for (Location location : interests) {
				latSum += Double.parseDouble(location.getLatitude());
				lonSum += Double.parseDouble(location.getLongitude());
			}
			latSum = latSum / interests.size() * 1.0;
			lonSum = lonSum / interests.size() * 1.0;
			
			meanCoord.setLatitude(latSum + "");
			meanCoord.setLongitude(lonSum + "");
			meanCoord.setArriveTime(interests.get(0).getTimeline());
			meanCoord.setLeaveTime(interests.get(interests.size() - 1).getTimeline());
			meanCoord.setAddress("UNHANDLE");
		} else {
			meanCoord = null;
		}
		
		return meanCoord;
	}
	
	class OutlierNodeDetect {
		private List<LofNode> getOutlierNodes(List<LofNode> nodes) {
			List<LofNode> list = getKDAndKN(nodes);
			list = calRearchableDistance(list);
			list = calRearchableDensity(list);
			list = calLof(list);
			Collections.sort(list, new LofComparator());
			
			List<LofNode> outliers = new ArrayList<LofNode>();
			for (LofNode lofNode : list) {
				if (lofNode.getLof() >= configuration.getLofThreshold()) {
					outliers.add(lofNode);
				}
			}
			
			return outliers;
		}
		
		private List<LofNode> getKDAndKN(List<LofNode> nodes) {
			List<LofNode> list = new ArrayList<LofNode>();
			for (int i = 0; i < nodes.size(); ++i) {
				LofNode pnode = new LofNode(nodes.get(i).getId(), nodes.get(i).getLocation());
				List<LofNode> kNeighborNodes = new ArrayList<LofNode>();
				for (int j = 0; j < nodes.size(); ++j) {
					LofNode qnode = new LofNode(nodes.get(j).getId(), nodes.get(j).getLocation());
					qnode.setDistance(pnode.getLocation().calDistanceTo(qnode.getLocation()));
					kNeighborNodes.add(qnode);
				}
				Collections.sort(kNeighborNodes, new DistComparator());
				for (int k = 1; k <= configuration.getMinPts(); ++k) {
					pnode.getkNeighbors().add(kNeighborNodes.get(k));
					if (k == configuration.getMinPts()) {
						pnode.setkDistance(kNeighborNodes.get(k).getDistance());
					}
				}
				list.add(pnode);
			}
			
			return list;
		}
		
		private List<LofNode> calRearchableDistance(List<LofNode> nodes) {
			for (LofNode pnode : nodes) {
				List<LofNode> kNeighborNodes = pnode.getkNeighbors();
				for (LofNode qnode : kNeighborNodes) {
					double kDistance = getKDistance(qnode.getId(), nodes);
					qnode.setReachableDistance(Math.max(kDistance, qnode.getDistance()));
				}
				pnode.setkNeighbors(kNeighborNodes);
			}
			
			return nodes;
		}
		
		private List<LofNode> calRearchableDensity(List<LofNode> nodes) {
			for (LofNode pnode : nodes) {
				List<LofNode> kNeighborNodes = pnode.getkNeighbors();
				double reachDisSum = 0.0, lrd = 0.0;
				for (LofNode qnode : kNeighborNodes) {
					reachDisSum += qnode.getReachableDistance();
				}
				lrd = configuration.getMinPts() / reachDisSum * 1.0;
				pnode.setReachableDensity(lrd);
			}
			
			return nodes;
		}
		
		private List<LofNode> calLof(List<LofNode> nodes) {
			for (LofNode pnode : nodes) {
				List<LofNode> kNeighborNodes = pnode.getkNeighbors();
				double lof = 0.0;
				for (LofNode qnode : kNeighborNodes) {
					double lrd = getLRD(qnode.getId(), nodes);
					lof += (lrd / pnode.getReachableDensity() * 1.0);
				}
				lof = lof / configuration.getMinPts() * 1.0;
				pnode.setLof(lof);
			}
			
			return nodes;
		}
		
		private double getKDistance(int id, List<LofNode> nodes) {
			double kDistance = 0.0;
			for (LofNode node : nodes) {
				if (id == node.getId()) {
					kDistance = node.getkDistance();
					break;
				}
			}
			
			return kDistance;
		}
		
		private double getLRD(int id, List<LofNode> nodes) {
			double lrd = 0.0;
			for (LofNode node : nodes) {
				if (id == node.getId()) {
					lrd = node.getReachableDensity();
					break;
				}
			}
			
			return lrd;
		}
		
		class DistComparator implements Comparator<LofNode> {
			@Override
			public int compare(LofNode o1, LofNode o2) {
				return o1.getDistance() - o2.getDistance() < 0 ? -1 : 1;
			}
		}
		
		class LofComparator implements Comparator<LofNode> {
			@Override
			public int compare(LofNode o1, LofNode o2) {
				return o1.getLof() - o2.getLof() < 0 ? -1 : 1;
			}
		}
	}
}
