package com.hty.sound4pi;

import java.io.InputStream;
import java.net.Socket;

public class Test1 {

	public static void main(String[] args) throws Exception {
		
		Socket s = new Socket("localhost", 7400);
		String msg = "{appId:'test', join:'true', type:'1', wait:'true', data:'你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好'}";
		//String msg = "{appId:'test', join:'true', type:'2', wait:'false', data:'D:/123.mp3'}";
		s.getOutputStream().write(msg.getBytes("UTF-8"));
		s.shutdownOutput();
		byte[] bs = new byte[1024];
		int len = 0;
		InputStream ips = s.getInputStream();
		while((len = ips.read(bs)) != -1 ){
			System.out.println(new String(bs, 0, len));
		}
		s.shutdownInput();
		s.close();
	}

}
