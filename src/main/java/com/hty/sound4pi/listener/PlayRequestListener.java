package com.hty.sound4pi.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PlayRequestListener implements Runnable {

	private static final Log logger = LogFactory.getLog(PlayRequestListener.class);

	private int port;
	
	public PlayRequestListener(int port) {
		logger.info("Start listener service...");
		this.port = port;
	}
	
	@Override
	public void run() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(port);
			while(true) {
				logger.info("Listening now.");
				Socket s = ss.accept();
				logger.info("Get play request.");
				Thread t = new Thread(new RequestSocketHandler(s));
				t.start();
			}
		} catch (IOException e) {
			logger.error("Start listener service failed.");
			e.printStackTrace();
			System.exit(0);
		} finally {
			try {
				ss.close();
			} catch (IOException e) { }
		}
		
	}

}
