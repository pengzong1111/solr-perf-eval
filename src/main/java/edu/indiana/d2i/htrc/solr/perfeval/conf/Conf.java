package edu.indiana.d2i.htrc.solr.perfeval.conf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Conf {
	private static Logger logger = Logger.getLogger(Conf.class);
	private static Properties properties = new Properties();
	static {
		try {
			properties.load(new FileInputStream("conf/config.properties"));
		} catch (FileNotFoundException e) {
			logger.error("not config file: conf/config.properties", e);
		} catch (IOException e) {
			logger.error("IOException happened while reading config file: conf/config.properties", e);
		}
	}
	
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
}
