package com.hty.sound4pi.util;

import java.util.HashMap;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.hty.sound4pi.Main;

public class BaiduConvertor {
    
	public static byte[] convert(String words) {
		PropertiesUtil putil = Main.getPropertiesUtil();
		// 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(putil.getProperty("AppID"), putil.getProperty("APIKey"), putil.getProperty("SecretKey"));

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
        
        // 设置可选参数
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", putil.getProperty("spd"));
        options.put("pit", putil.getProperty("pit"));
        options.put("per", putil.getProperty("per"));
		// 调用接口
        TtsResponse res = client.synthesis(words, "zh", 1, options);
        byte[] data = res.getData();
        if(res.getErrorCode() == 0) {
        	System.out.println("合成成功：" + words);
        } else {
        	System.out.println("合成失败：" + words);
        }
        return data;
	}
}
