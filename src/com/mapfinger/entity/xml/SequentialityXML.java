package com.mapfinger.entity.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import com.mapfinger.log.FileLogger;

public class SequentialityXML {
	private Document doc;
	private Element root;
	
	public SequentialityXML() {
		this.doc = DocumentHelper.createDocument();
		this.root = doc.addElement(XmlNode.ROOT_NAME);
	}
	
	public Document getDoc() {
		return doc;
	}
	
	public Element getRoot() {
		return root;
	}
	
	public boolean store(String path) {
		try {
			File file = new File(path);
			OutputStream out = new FileOutputStream(file);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(doc);
			writer.flush();
			writer.close();
			
			return true;
		} catch (Exception e) {
			FileLogger.getLogger().error("Failed to persistance " + path + ".Err:" + e.getMessage());
			
			return false;
		}
	}
}
