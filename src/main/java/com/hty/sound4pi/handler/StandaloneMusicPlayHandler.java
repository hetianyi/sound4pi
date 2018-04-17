package com.hty.sound4pi.handler;

import com.hty.sound4pi.bean.PlayRequest;
import com.hty.sound4pi.util.Mp3Player;
import com.hty.sound4pi.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileNotFoundException;

/**
 * 独立模式播放，不同于消息模式播放，该模式在单独的线程中播放，不会打断消息模式的播放，不会排队，
 * 但是如果有新的独立播放请求，则正在播放的独立播放被打断而播放新的请求。
 * @author Hetianyi
 */
public class StandaloneMusicPlayHandler implements Runnable {

	private static final Log logger = LogFactory.getLog(StandaloneMusicPlayHandler.class);

	private PlayRequest req;
	
	private Mp3Player player;
	public void setRequest(PlayRequest req) {
		this.req = req;
		String cmd = req.getCmd();
		if(null != cmd && "pause".equals(cmd)) {
			if(null != player) {
				logger.info("Play [pause]");
				player.pause();
			}
		} else if(null != cmd && "start".equals(cmd)) {
			if(null != player) {
				logger.info("Play [start]");
				player.begin();
			}
		}
		if(StringUtil.isEmpty(cmd) && null != player) {
			player.stop();
		}
	}
	
	private void play() {
		while(true) {
			if(null != req) {
				String path = req.getData();
				player = new Mp3Player();
				try {
					logger.info("Play " + path);
					this.req = null;
					if(StringUtil.isEmpty(path)) {
						player.stop();
					}
					player.play(path);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					logger.error("Play in standalone mode failed：" + path);
					player = null;
					req = null;
					//MessagePlayHandler.playError("stand_alone_music_play_error.mp3", "哎呀，要播放的音乐找不到了");
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { }
		}
	}
	
	@Override
	public void run() {
		play();
	}
	
}
