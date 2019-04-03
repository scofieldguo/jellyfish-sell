package com.jellyfish.sell.support;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5EncryptUtil {
	
	/** 用来将字节转换成 16 进制表示的字符 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * MD5加密
	 * @param text
	 * @return
	 */
	public static String md5Enc (String text, String charset) {
		MessageDigest msgDigest = null;
	    try {
	    	msgDigest  = MessageDigest.getInstance("MD5");
	    } catch (NoSuchAlgorithmException e) {
	    }
	    try {
			msgDigest.update(text.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
		}
        byte[] tmp = msgDigest.digest();
        int l = tmp.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & tmp[i]) >>> 4];
			out[j++] = DIGITS[0x0F & tmp[i]];
		}
	    return new String(out);
	}
	
	public static String md5Enc16 (String text, String charset) {
		if (StringUtils.isEmpty(text)) {
			return null;
		}
		MessageDigest msgDigest = null;
	    try {
	    	msgDigest  = MessageDigest.getInstance("MD5");
	    } catch (NoSuchAlgorithmException e) {
	    }
	    try {
			msgDigest.update(text.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
		}
        byte[] tmp = msgDigest.digest();
        // MD5 的计算结果是一个 128 位的长整数，
        int l = tmp.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & tmp[i]) >>> 4];
			out[j++] = DIGITS[0x0F & tmp[i]];
		}
	    return new String(out).substring(8, 24);
	}



	/**
	 * @Description:加密-32位小写
	 * @author:liuyc
	 * @time:2016年5月23日 上午11:15:33
	 */
	public static String encrypt32(String encryptStr) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] md5Bytes = md5.digest(encryptStr.getBytes());
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
                    hexValue.append("0");
                }
				hexValue.append(Integer.toHexString(val));
			}
			encryptStr = hexValue.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return encryptStr;
	}

	
	public static void main(String[] args) {
		System.out.println(md5Enc("MYYcGB!nTd","UTF-8"));
	}
}
