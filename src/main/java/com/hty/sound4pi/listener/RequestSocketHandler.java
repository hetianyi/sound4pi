package com.hty.sound4pi.listener;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONObject;

import com.hty.sound4pi.bean.PlayRequest;
import com.hty.sound4pi.handler.MessagePlayHandler;

public class RequestSocketHandler implements Runnable {
	
	private Socket client;
	
	public RequestSocketHandler(Socket client) {
		this.client = client;
		
	}
	
	@Override
	public void run() {
		try {
			InputStream ips = client.getInputStream();
			byte[] bs = new byte[10240];
			int len = 0;
			StringBuilder sb = new StringBuilder();
			while((len = ips.read(bs)) != -1) {
				sb.append(new String(bs, 0, len, "UTF-8"));
			}
			client.shutdownInput();
			OutputStream ops = client.getOutputStream();
			ops.write("gotit!".getBytes("UTF-8"));
			ops.flush();
			client.shutdownOutput();
			
			JSONObject obj = new JSONObject(sb.toString());
			PlayRequest req = new PlayRequest();
			Map<String, Object> map = obj.toMap();
			BeanUtils.populate(req, map);
			if(req.isWait())
				PlayList.add(req);
			else {
				MessagePlayHandler.playStandAlone(req);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
