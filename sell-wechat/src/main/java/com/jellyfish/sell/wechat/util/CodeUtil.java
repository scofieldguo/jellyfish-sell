package com.jellyfish.sell.wechat.util;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public class CodeUtil {
	/**
	 * 错误码-提示信息
	 */
	private static final Map<Integer, String> CODE_MSG = new HashMap<Integer, String>();
	
	private static String getConfig(Integer code) {
		Properties prop = new Properties();
		try {
			prop.load(new InputStreamReader(CodeUtil.class.getClassLoader()
					.getResourceAsStream("error.properties"),"UTF-8"));
			Iterator<Entry<Object, Object>> keys = prop.entrySet().iterator();
			while(keys.hasNext()){
				Entry<Object, Object> entry = keys.next();
				CODE_MSG.put(Integer.valueOf(entry.getKey().toString()),entry.getValue().toString());
			}
			return CODE_MSG.get(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getMSGByCode(Integer code){
		String msg = CODE_MSG.get(code);
		if(msg == null || "".equals(msg)){
			return getConfig(code);
		}
		return msg;
	}
}
