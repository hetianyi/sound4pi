package com.hty.sound4pi.listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PlayRequestListener implements Runnable {
	
	private int port;
	
	public PlayRequestListener(int port) {
		System.out.println("启动监听服务...");
		this.port = port;
	}
	
	@Override
	public void run() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(port);
			while(true) {
				System.out.println("正在监听...");
				Socket s = ss.accept();
				System.out.println("收到请求");
				Thread t = new Thread(new RequestSocketHandler(s));
				t.start();
			}
		} catch (IOException e) {
			System.out.println("监听服务启动失败...");
			e.printStackTrace();
			System.exit(0);
		} finally {
			try {
				ss.close();
			} catch (IOException e) { }
		}
		
	}

}
