package com.jellyfish.sell.support;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;

public class OrderUtil {

	public static String createOrder(String prefix,int random,String suffix) {
		Date now = new Date();
		StringBuffer sb = new StringBuffer();
		sb.append(prefix)
				.append(DateUtils.formatDate(now, DateUtils.DatePattern.PATTERN_ALL_NOSPLIT_EXTENDS.getPattern()));

		if(random >0) {
			String randomNum = RandomStringUtils.randomNumeric(random);
			sb.append(randomNum);
		}
		sb.append(suffix);
		return sb.toString();
	}

	public static void main(String[] args) {
		String userStr = createOrder("P",2,"144");
		System.out.println(userStr);
		String userIdRtr = userStr.substring(20,userStr.length());
		System.out.println(userIdRtr);
	}
}
