package com.hty.sound4pi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.hty.sound4pi.handler.MessagePlayHandler;
import com.hty.sound4pi.listener.PlayRequestListener;
import com.hty.sound4pi.util.PropertiesUtil;
import com.hty.sound4pi.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 服务启动类，默认在端口7400开启服务
 * @author Hetianyi
 */
public class Main {

	private static final Log logger = LogFactory.getLog(Main.class);
	
	private static int port = 7400;
	
	private static File tempDir = null;
	//系统属性配置类
	private static PropertiesUtil putil;
	
	private static File configFile;
	// jar文件所在文件夹
	private static File classpath;
	
	public static PropertiesUtil getPropertiesUtil() {
		return putil;
	}
	/**
	 * 获取服务端口
	 * @return
	 */
	public static int getPort() {
		return port;
	}
	/**
	 * 获取语音缓存目录
	 * @return
	 */
	public static File getTempDir() {
		return tempDir;
	}
	
	/**
	 * 语音文件缓存目录
	 * @return
	 */
	private static void createTempDir() {
		tempDir = new File(configFile.getParentFile(), "temp");
		if(!tempDir.exists()) {
			tempDir.mkdirs();
		}
	}
	
	
	/**
	 * 从java启动参数获取配置文件
	 * @param args
	 * @return
	 * @throws FileNotFoundException
	 */
	private static File parseConfigFile() throws FileNotFoundException {
		File file = new File(classpath, "sound.properties");
		if(file.exists() && file.isFile() && file.canRead()) {
			return file;
		} else {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
	}
	/**
	 * 设置服务端口
	 */
	private static void setPort() {
		String _port = putil.getProperty("port");
		if(null != _port && _port.matches("[0-9]+")) {
			port = Integer.valueOf(_port);
		}
	}
	/**
	 * 校验参数合法性
	 */
	private static void checkConfig() {
		String AppID = putil.getProperty("AppID");
		if(StringUtil.isEmpty(AppID)) {
			throw new IllegalArgumentException("参数错误：无效的AppID");
		}
		String APIKey = putil.getProperty("APIKey");
		if(StringUtil.isEmpty(APIKey)) {
			throw new IllegalArgumentException("参数错误：无效的APIKey");
		}
		String SecretKey = putil.getProperty("SecretKey");
		if(StringUtil.isEmpty(SecretKey)) {
			throw new IllegalArgumentException("参数错误：无效的SecretKey");
		}
		String spd = putil.getProperty("spd");
		if(StringUtil.isEmpty(spd) || !spd.trim().matches("[0-9]")) {
			throw new IllegalArgumentException("参数错误：spd必须是0-9的数字");
		}
		String pit = putil.getProperty("pit");
		if(StringUtil.isEmpty(pit) || !pit.trim().matches("[0-9]")) {
			throw new IllegalArgumentException("参数错误：pit必须是0-9的数字");
		}
		String per = putil.getProperty("per");
		if(StringUtil.isEmpty(per) || !per.trim().matches("[0-4]")) {
			throw new IllegalArgumentException("参数错误：per必须是0-4的数字");
		}
	}
	
	public static void main(String[] args) throws IOException {
		classpath = new File(System.getProperty("java.class.path")).getParentFile();
		configFile = parseConfigFile();
		if(!configFile.exists() || configFile.isDirectory()  || !configFile.getName().endsWith("properties")) {
			throw new IllegalArgumentException("无效的配置文件：" + configFile.getAbsolutePath());
		}
		createTempDir();
		putil = new PropertiesUtil(configFile);
		//Set listening port.
		setPort();
		checkConfig();
		logger.info("Sound 4 pi is listening on port : " + port);
		PlayRequestListener listener = new PlayRequestListener(port);
		Thread tlistener = new Thread(listener);
		tlistener.start();
		Thread thandler = new Thread(new MessagePlayHandler());
		thandler.start();
	}
	
	
	
	
}
