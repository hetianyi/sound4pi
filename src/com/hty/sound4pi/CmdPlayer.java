package com.hty.sound4pi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.hty.sound4pi.util.PropertiesUtil;
/**
 * 命令行操作类，控制独立线程播放音乐的暂停和开始，以及播放自定义文本合成的语音
 * @author Hetianyi
 */
public class CmdPlayer {
	//系统属性配置类
	private static PropertiesUtil putil;
	
	private static File configFile;
	// jar文件所在文件夹
	private static File classpath;
	
	private static int port = 7400;
		
	private static File parsePlayFile(String[] args) throws IOException {
		if(args.length > 0) {
			String path = args[0];
			//如果是其他命令
			//暂停正在播放的音乐
			if("pause".equals(path)) {
				String msg = "{appId:'rand', join:'false', type:'1', wait:'false', data:'', cmd: 'pause'}";
				sendRequest(msg);
				return null;
			}
			//播放暂停的音乐
			if("start".equals(path)) {
				String msg = "{appId:'rand', join:'false', type:'1', wait:'false', data:'', cmd: 'start'}";
				sendRequest(msg);
				return null;
			}
			//调用接口，播放合成语音
			if("say".equals(path)) {
				if(args.length > 1) {
					StringBuilder words = new StringBuilder();
					for(int i = 1; i < args.length; i++) {
						words.append(args[i] + "，");
					}
					String msg = "{appId:'rand', join:'false', type:'1', wait:'true', data:'"+ words.toString().replace("\"", "，") +"'}";
					sendRequest(msg);
					return null;
				} else {
					System.out.println("\n 请输入要播放的话。");
					showUsage();
					return null;
				}
			}
			
			
			
			File file = new File(path);
			if(!file.exists() || file.isDirectory() || !file.getName().endsWith(".mp3")) {
				System.out.println("\n " + file.getAbsolutePath() + " 不存在或不是有效的mp3文件.");
				showUsage();
				return null;
			}
			return file;
		}
		System.out.println("\n 没有指定播放的文件");
		showUsage();
		System.exit(0);
		return null;
	}
	
	private static void sendRequest(String msg) throws IOException {
		Socket s = new Socket("localhost", port);
		s.getOutputStream().write(msg.getBytes("UTF-8"));
		s.shutdownOutput();
		byte[] bs = new byte[1024];
		int len = 0;
		InputStream ips = s.getInputStream();
		while((len = ips.read(bs)) != -1 ){
			//System.out.println(new String(bs, 0, len));
		}
		s.shutdownInput();
		s.close();
		System.out.println("已请求，请稍候...");
	}
	
	private static void showUsage() {
		System.out.println("\n usage: piplayer [xxx.mp3][pause][start][say xxx]\n");
	}
	
	/**
	 * 从java启动参数获取配置文件
	 * @param args
	 * @return
	 * @throws FileNotFoundException
	 */
	private static File parseConfigFile(String[] args) throws FileNotFoundException {
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
	
	public static void main(String[] args) throws IOException {
		classpath = new File(System.getProperty("java.class.path")).getParentFile();
		configFile = parseConfigFile(args);
		if(!configFile.exists() || configFile.isDirectory()  || !configFile.getName().endsWith("properties")) {
			throw new IllegalArgumentException("无效的配置文件：" + configFile.getAbsolutePath());
		}
		putil = new PropertiesUtil(configFile);
		setPort();
		File file = parsePlayFile(args);
		if(null != file) {
			String msg = "{appId:'rand', join:'false', type:'1', wait:'false', data:'"+ file.getAbsolutePath().replace("\\", "/") +"'}";
			sendRequest(msg);
		}
	}

}
