package com.hty.sound4pi.handler;

import com.hty.sound4pi.Main;
import com.hty.sound4pi.bean.PlayRequest;
import com.hty.sound4pi.listener.PlayList;
import com.hty.sound4pi.util.BaiduConvertor;
import com.hty.sound4pi.util.Mp3Player;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

public class MessagePlayHandler implements Runnable {

	private static final Log logger = LogFactory.getLog(MessagePlayHandler.class);

	private static StandaloneMusicPlayHandler standaloneMusicPlayHandler;
	
	@Override
	public void run() {
		while(true) {
			try {
				PlayRequest req = PlayList.getPlayRequest();
				Mp3Player player = new Mp3Player();
				if(req.getType().equals("1")) {
					System.out.println("Play " + req.getData());
					byte[] bs = BaiduConvertor.convert(req.getData());
					ByteArrayInputStream ips = new ByteArrayInputStream(bs);
					player.play(ips);
					ips.close();
				} else if(req.getType().equals("2")) {
					File file = new File(req.getData());
					if(file.exists() && file.isFile()) {
						logger.error("Play from local file " + file.getAbsolutePath());
						player.play(file.getAbsolutePath());
					} else {
						logger.error("Sorry, Play failed(3)");
						String play_error_file = "play_error.mp3";
						playError(play_error_file, "Sorry, I cannot play the message!");
					}
				} else {
					logger.error("Sorry, Play failed(1)");
					String play_error_file = "play_error.mp3";
					playError(play_error_file, "Sorry, I cannot play the message!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Sorry, Play failed(2)");
				String play_error_file = "play_error.mp3";
				playError(play_error_file, "Sorry, I cannot play the message!");
			}
		}
	}
	
	public static void playStandAlone(PlayRequest req) {
		if(null == standaloneMusicPlayHandler) {
			standaloneMusicPlayHandler = new StandaloneMusicPlayHandler();
			Thread t = new Thread(standaloneMusicPlayHandler);
			t.start();
		}
		standaloneMusicPlayHandler.setRequest(req);
	}
	
	
	public static void playError(String play_error_file, String errorWords) {
		File file = new File(Main.getTempDir(), play_error_file);
		Mp3Player player = new Mp3Player();
		if(file.exists()) {
			InputStream ips = null;
			try {
				ips = new FileInputStream(file.getAbsolutePath());
				player.play(ips);
				ips.close();
			} catch (Exception e1) { } finally {
				try {
					ips.close();
				} catch (IOException e) { }
			}
		} else {
			String words = errorWords;
			byte[] bs = BaiduConvertor.convert(words);
			try {
				OutputStream ops = new FileOutputStream(file.getAbsolutePath());
				ops.write(bs);
				ops.close();
				try {
					player.play(file.getAbsolutePath());
				} catch (FileNotFoundException e1) { }
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
