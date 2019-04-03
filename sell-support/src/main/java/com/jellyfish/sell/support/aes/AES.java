package com.jellyfish.sell.support.aes;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *  AES-128-CBC加解密模式
 */
public class AES {
	private static final String CHARSET = "UTF-8";
	public static final String KEY = "vFru2myYrb1E08mm";
	public static final String IV = "2017121719005099";
	/**
	 * 加密
	 * 
	 * @param data 要加密的数据
	 * @return 密文， 16进制
	 * @throws Exception
	 */
	public static String encrypt(String data) {
		if(null == data){
			return null;
		}
		try{
			SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
			byte[] encrypted = cipher.doFinal(data.getBytes(CHARSET));
			return HexUtils.encodeHexString(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加密
	 * 
	 * @param data 要加密的数据
	 * @return 密文， 16进制
	 * @throws Exception
	 */
	public static String encrypt(String data, String key, String iv) throws Exception {
		if(null == data){
			return null;
		}
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
		byte[] encrypted = cipher.doFinal(data.getBytes(CHARSET));
		return HexUtils.encodeHexString(encrypted);
	}
	
	/**
	 * 解密
	 * 
	 * @param data 要解密的数据， 16进制
	 * @return 明文
	 * @throws Exception
	 */
	public static String decrypt(String data) {
		if(null == data){
			return null;
		}
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
			byte[] decrypted = cipher.doFinal(HexUtils.decode(data));
			return new String(decrypted, CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解密
	 * 
	 * @param data 要解密的数据， 16进制
	 * @param key
	 * @param iv
	 * @return 明文
	 * @throws Exception
	 */
	public static String decrypt(String data, String key, String iv) throws Exception {
		if(null == data){
			return null;
		}
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
		byte[] decrypted = cipher.doFinal(HexUtils.decode(data));
		return new String(decrypted, CHARSET);
	}
	
	public static final void main(String[] args) throws Exception{
		String data = "101";
		data = encrypt(data, "lFru2moYrb1E1Nvl", "2017121510050688");
		System.out.println("encrypt:" + data);
		data = "39|1000";
		data = encrypt(data);
		System.out.println(decrypt(data).split("\\|")[0]);
		System.out.println("encrypt:"+data+ "=="+ data.length());
		
		
	}
	
}
