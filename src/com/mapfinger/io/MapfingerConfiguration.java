package com.mapfinger.io;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class MapfingerConfiguration {
	private static MapfingerConfiguration configuration;
	
	private Map<String, String> configMap;
	
	private static final String PARAM_DIST = "distThreshold";
	private static final String PARAM_TIME = "timeThreshold";
	private static final String PARAM_SLOPEROTATE = "slopeRotateThreshold";
	private static final String PARAM_SLOPEROTATERATION = "slopeRotateRation";
	private static final String PARAM_MINPTS = "minPts";
	private static final String PARAM_LOF = "lofThreshold";
	private static final String PARAM_R = "R";
	
	private static final String PATH_ORIG = "origen";
	private static final String PATH_CONV = "convert";
	private static final String PATH_KEY = "key";
	private static final String PATH_KEYCLU = "keycluster";
	private static final String PATH_SEQ = "sequentiality";
	private static final String PATH_PATTERN = "pattern";
	private static final String PATH_KEY_OLD = "key-old";
	private static final String PATH_KEYCLU_OLD = "keycluster-old";
	private static final String PATH_SEQ_OLD = "sequentiality-old";
	private static final String PATH_PAT_OLD = "pattern-old";
	
	private MapfingerConfiguration() {
		try {
			configMap = new HashMap<String, String>();
			String configPath = System.getProperty("user.dir") + "/resources/mapfinger-conf.xml";
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(configPath));
			Element root = document.getRootElement();
			
			Iterator<?> pelements = root.elementIterator(), celements;
			while (pelements.hasNext()) {
				celements = ((Element) pelements.next()).elementIterator();
				while (celements.hasNext()) {
					List<?> attributes = ((Element) celements.next()).attributes();
					configMap.put(((Attribute) attributes.get(0)).getValue(),
							((Attribute) attributes.get(1)).getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static MapfingerConfiguration getMapfingerConfiguration() {
		if (configuration == null) {
			configuration = new MapfingerConfiguration();
		}
		
		return configuration;
	}
	
	public double getDistThreshold() {
		// meter
		return Double.parseDouble(configMap.get(PARAM_DIST));
	}
	
	public double getTimeThreshold() {
		// second
		return Double.parseDouble(configMap.get(PARAM_TIME));
	}
	
	public double getSlopeRotateThreshold() {
		// ration
		return Double.parseDouble(configMap.get(PARAM_SLOPEROTATE));
	}
	
	public double getSlopeRotateRation() {
		// ration
		return Double.parseDouble(configMap.get(PARAM_SLOPEROTATERATION));
	}
	
	public double getMinPts() {
		// ration
		return Double.parseDouble(configMap.get(PARAM_MINPTS));
	}
	
	public double getLofThreshold() {
		// ration
		return Double.parseDouble(configMap.get(PARAM_LOF));
	}
	
	public double getR() {
		// kilometer
		return Double.parseDouble(configMap.get(PARAM_R));
	}
	
	public String getOrigDataPath() {
		return configMap.get(PATH_ORIG);
	}
	
	public String getConvDataPath() {
		return configMap.get(PATH_CONV);
	}
	
	public String getKeyDataPath() {
		return configMap.get(PATH_KEY);
	}
	
	public String getKeycluDataPath() {
		return configMap.get(PATH_KEYCLU);
	}
	
	public String getSeqDataPath() {
		return configMap.get(PATH_SEQ);
	}
	
	public String getPatternDataPath() {
		return configMap.get(PATH_PATTERN);
	}
	
	public String getOldKeyDataPath() {
		return configMap.get(PATH_KEY_OLD);
	}
	
	public String getOldKeycluDataPath() {
		return configMap.get(PATH_KEYCLU_OLD);
	}
	
	public String getOldSeqDataPath() {
		return configMap.get(PATH_SEQ_OLD);
	}
	
	public String getOldPatternDataPath() {
		return configMap.get(PATH_PAT_OLD);
	}
	
}
