package com.mapfinger.test;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataMaker {
	private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final long DIFF = 5000;
	
	private static final String FILENAME = "/resource/20151109.txt";
	
	public static void main(String[] args) throws Exception {
		List<String> list = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat(PATTERN);
		
		String start = "2015-11-09 00:00:04";
		long stime = format.parse(start).getTime(), ctime = stime;
		while ((ctime - stime) / (1000 * 60 * 60) < 24) {
			list.add(format.format(new Date(ctime)));
			System.out.println(list.size());
			ctime += DIFF;
		}
		
		String filepath = System.getProperty("user.dir") + FILENAME;
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filepath)));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		for (String s : list) {
			writer.write(s + ",");
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}
}
