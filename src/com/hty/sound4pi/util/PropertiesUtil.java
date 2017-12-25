package com.hty.sound4pi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 读取类路径下properties文件的工具类
 * @author Hetianyi
 * @version 1.0
 */
public class PropertiesUtil {
	/** 属性集合 */
	private Map<String, String> properties = new HashMap<String, String>();
	
	public PropertiesUtil(File configFile) throws IOException {
		parseConfigFile(configFile);
	}
	
	public PropertiesUtil(String filePath) throws IOException {
		parseConfigFile(new File(filePath));
	}
	
	private void parseConfigFile(File configFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(configFile));
		String line = null;
		while((line = reader.readLine()) != null) {
			line = line.trim();
			String[] st = line.split("=");
			if(st.length == 2) {
				properties.put(st[0].trim(), st[1].trim());
			}
		}
		reader.close();
	}
	/**
	 * 获取属性集合
	 * @return
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
	/**
	 * 获取一个属性值
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return properties.get(key);
	}
	
	
}
