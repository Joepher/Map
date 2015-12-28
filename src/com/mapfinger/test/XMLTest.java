package com.mapfinger.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XMLTest {
	private static String path = "resources/mapfinger-conf.xml";
	
	static void write() throws Exception {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("root");
		Element parent, child;
		
		for (int i = 0; i < 5; i++) {
			parent = root.addElement("parent");
			parent.addAttribute("arr", "arr" + i);
			parent.addAttribute("lea", "leave" + i);
			parent.addAttribute("lat", "lat" + i);
			parent.addAttribute("addr", "addr" + i);
			
			for (int j = 0; j < 5; j++) {
				child = parent.addElement("child");
				child.addAttribute("lat", "lat" + i + j);
				child.addAttribute("lon", "lon" + i + j);
			}
		}
		
		File file = new File(path);
		OutputStream out = new FileOutputStream(file);
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		
		XMLWriter writer = new XMLWriter(out, format);
		writer.write(doc);
		writer.flush();
		writer.close();
	}
	
	static void read() throws Exception {
		File file = new File(path);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
		Element root = doc.getRootElement();
		
		printElement(root);
		
		Element parent, child;
		Iterator<?> pelements = root.elementIterator(), celements;
		while (pelements.hasNext()) {
			parent = (Element) pelements.next();
			
			printElement(parent);
			
			celements = parent.elementIterator();
			while (celements.hasNext()) {
				child = (Element) celements.next();
				
				printElement(child);
			}
		}
	}
	
	static void printElement(Element element) {
		System.out.print(element.getName());
		
		List<?> attrs = element.attributes();
		Attribute attr;
		for (int i = 0; i < attrs.size(); i++) {
			attr = (Attribute) attrs.get(i);
			System.out.print(" " + attr.getName() + ":" + attr.getValue());
		}
		System.out.print("\n");
	}
	
	public static void main(String[] args) throws Exception {
		read();
	}
}
