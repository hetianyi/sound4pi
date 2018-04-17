package com.hty.sound4pi.util;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.hty.sound4pi.Main;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

public class BaiduConvertor {

    private static final Log logger = LogFactory.getLog(BaiduConvertor.class);


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
        org.json.JSONObject result = res.getResult();
        if(null != data) {
            logger.info("Synthetic success for words: \"" + words + "\"");
        } else {
            logger.error("Synthetic failed for words: \"" + words + "\"");
        }
        return data;
	}
}
