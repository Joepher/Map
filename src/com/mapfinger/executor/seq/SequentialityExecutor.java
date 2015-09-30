package com.mapfinger.executor.seq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.dom4j.Element;
import com.mapfinger.entity.UserData;
import com.mapfinger.entity.location.Coord;
import com.mapfinger.entity.location.KeyPoint;
import com.mapfinger.entity.location.Location;
import com.mapfinger.entity.xml.SequentialityXML;
import com.mapfinger.entity.xml.XmlNode;
import com.mapfinger.executor.BaseExecutor;
import com.mapfinger.executor.data.LocationDataExtractExecutor;
import com.mapfinger.io.FileOperator;

public class SequentialityExecutor extends BaseExecutor {
	private String convDataPath;
	private String keyenhDataPath;
	private String seqPath;
	
	private static final double timeThreshold = 60.0;// s
	
	public SequentialityExecutor(UserData userData) {
		super(userData);
		String home = FileOperator.getUserHome(userData.getUserName());
		this.convDataPath = home + "bd09ll/" + userData.getFileName();
		this.keyenhDataPath = home + "keyenh/" + userData.getFileName();
		String filename = userData.getFileName().replace("plt", "xml");
		this.seqPath = home + "seq/" + filename;
	}
	
	@Override
	public boolean execute() {
		List<Location> locList;
		List<Coord> pathList;
		List<KeyPoint> keyPoints = new LocationDataExtractExecutor(null).extractKeyPoint(keyenhDataPath);
		List<Location> locations = new LocationDataExtractExecutor(null)
				.extractConvLocationData(convDataPath);
		
		int keyNum = keyPoints.size(), locNum = locations.size();
		if (keyNum == 0) {
			return false;
		}
		
		int locPos = 0, preKeyPos = 0, curKeyPos = preKeyPos + 1;
		SequentialityXML seqXml = new SequentialityXML();
		Element keyNode;
		
		keyNode = addKeyNode(seqXml.getRoot(), keyPoints.get(preKeyPos));
		while (curKeyPos < keyNum) {
			locList = new ArrayList<Location>();
			while (locPos < locNum) {
				boolean isAfterArrive = keyPoints.get(preKeyPos).getLeaveTime()
						.compareTo(locations.get(locPos).getTimeline()) < 0 ? true : false;
				boolean isBeforeLeave = keyPoints.get(curKeyPos).getArriveTime()
						.compareTo(locations.get(locPos).getTimeline()) > 0 ? true : false;
				if (!isAfterArrive && isBeforeLeave) {
					++locPos;
				} else if (isAfterArrive && isBeforeLeave) {
					locList.add(locations.get(locPos));
					++locPos;
				} else {
					break;
				}
			}
			
			pathList = getPath(locList);
			
			for (Coord coord : pathList) {
				addPathNoe(keyNode, coord);
			}
			
			keyNode = addKeyNode(seqXml.getRoot(), keyPoints.get(curKeyPos));
			preKeyPos = curKeyPos;
			curKeyPos = preKeyPos + 1;
		}
		
		return seqXml.store(seqPath);
	}
	
	private Element addKeyNode(Element root, KeyPoint keyPoint) {
		Element node = root.addElement(XmlNode.KEY_NODE_NAME);
		node.addAttribute(XmlNode.ATTR_LONGITUDE, keyPoint.getLongitude());
		node.addAttribute(XmlNode.ATTR_LATITUDE, keyPoint.getLatitude());
		node.addAttribute(XmlNode.ATTR_ARRIVETIME, keyPoint.getArriveTime());
		node.addAttribute(XmlNode.ATTR_LEAVETIME, keyPoint.getLeaveTime());
		node.addAttribute(XmlNode.ATTR_ADDRESS, keyPoint.getAddress());
		
		return node;
	}
	
	private Element addPathNoe(Element parent, Coord coord) {
		Element node = parent.addElement(XmlNode.PATH_NODE_NAME);
		node.addAttribute(XmlNode.ATTR_LONGITUDE, coord.getLongitude());
		node.addAttribute(XmlNode.ATTR_LATITUDE, coord.getLatitude());
		
		return node;
	}
	
	private List<Coord> getPath(List<Location> locations) {
		List<Coord> coords = new ArrayList<Coord>();
		List<Location> list;
		int start = 0, end = 0;
		
		while (start < locations.size()) {
			list = new ArrayList<Location>();
			while (end < locations.size()) {
				if (getTimeDiff(locations.get(start), locations.get(end)) <= timeThreshold) {
					list.add(locations.get(end));
					++end;
				} else {
					break;
				}
			}
			coords.add(getMeanCoord(list));
			start = end;
		}
		
		return coords;
	}
	
	private double getTimeDiff(Location la, Location lb) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		double diff = 0.0;
		
		try {
			Date[] date = { format.parse(la.getTimeline()), format.parse(lb.getTimeline()) };
			
			diff = Math.abs((date[0].getTime() - date[1].getTime()) / 1000 * 1.0);
			
		} catch (ParseException e) {
			logger.error("Compute time diff Err:" + e.getMessage());
		}
		
		return diff;
	}
	
	private Coord getMeanCoord(List<Location> locations) {
		Coord coord = new Coord();
		double lonSum = 0.0, latSum = 0.0;
		
		for (Location location : locations) {
			lonSum += Double.valueOf(location.getLongitude());
			latSum += Double.valueOf(location.getLatitude());
		}
		
		coord.setLongitude(String.valueOf(lonSum / locations.size() * 1.0));
		coord.setLatitude(String.valueOf(latSum / locations.size() * 1.0));
		
		return coord;
	}
}
