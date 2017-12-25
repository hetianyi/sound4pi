package com.hty.sound4pi.listener;

import java.util.ArrayList;
import java.util.List;

import com.hty.sound4pi.bean.PlayRequest;

public class PlayList {
	
	public static List<PlayRequest> list = new ArrayList<PlayRequest>();
	
	public static void add(PlayRequest req) {
		try {
			synchronized (list) {
				if(req.isJoin()) {
					boolean contain = false;
					for(int i = 0; i < list.size(); i++) {
						if(req.getAppId().equals(list.get(i).getAppId())) {
							list.set(i, req);
							contain = true;
						}
					}
					if(!contain) {
						list.add(req);
					}
				} else {
					list.add(req);
				}
			}
		} catch (Exception e) {
			list.add(req);
		}
	}
	
	public static PlayRequest getPlayRequest() {
		while(list.size() == 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		synchronized (list) {
			return list.remove(0);
		}
	}
	
}
