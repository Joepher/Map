package com.mapfinger.test;

import com.mapfinger.io.MapfingerConfiguration;

public class MapfingerConfigurationTest {
	
	public static void main(String[] args) {
		MapfingerConfiguration configuration = MapfingerConfiguration.getMapfingerConfiguration();
		System.out.println(configuration.getDistThreshold());
		System.out.println(configuration.getTimeThreshold());
		System.out.println(configuration.getSlopeRotateThreshold());
		System.out.println(configuration.getSlopeRotateRation());
		System.out.println(configuration.getMinPts());
		System.out.println(configuration.getR());
		System.out.println(configuration.getOrigDataPath());
		System.out.println(configuration.getConvDataPath());
		System.out.println(configuration.getKeyDataPath());
		System.out.println(configuration.getKeycluDataPath());
		System.out.println(configuration.getSeqDataPath());
		System.out.println(configuration.getPatternDataPath());
		System.out.println(configuration.getOldKeyDataPath());
		System.out.println(configuration.getOldKeycluDataPath());
		System.out.println(configuration.getOldPatternDataPath());
		System.out.println(configuration.getOldSeqDataPath());
	}
	
}
