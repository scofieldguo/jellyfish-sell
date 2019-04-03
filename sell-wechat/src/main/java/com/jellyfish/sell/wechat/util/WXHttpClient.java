package com.jellyfish.sell.wechat.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

public class WXHttpClient {
	private static PoolingHttpClientConnectionManager poolConnManager;
	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 400;
	/**
	 * 获取连接的最大等待时间
	 */
	public final static int WAIT_TIMEOUT = 3000;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 200;
	/**
	 * 连接超时时间
	 */
	public final static int CONNECTION_TIMEOUT = 30000;
	/**
	 * 读取超时时间
	 */
	public final static int READ_TIMEOUT = 30000;

	public static CloseableHttpClient getHttpClient(String certPath,String mchid) {
		if(poolConnManager == null){
			try {
				KeyStore keyStore  = KeyStore.getInstance("PKCS12");
				FileInputStream instream = new FileInputStream(new File(certPath));  
	            keyStore.load(instream, mchid.toCharArray());  
	            SSLContext sslContext = SSLContexts.custom()  
	                    .loadKeyMaterial(keyStore, mchid.toCharArray())  
	                    .build();  
				HostnameVerifier hostNameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
				SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext,new String[] { "TLSv1" },null, hostNameVerifier);
				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
						.register("http", PlainConnectionSocketFactory.getSocketFactory())
						.register("https", sslFactory).build();
				SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(READ_TIMEOUT).build();
				poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
				poolConnManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
				poolConnManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
				poolConnManager.setDefaultSocketConfig(socketConfig);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(WAIT_TIMEOUT)
				.setConnectTimeout(CONNECTION_TIMEOUT)
				.setSocketTimeout(READ_TIMEOUT).build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(poolConnManager)
				.setDefaultRequestConfig(requestConfig).build();
		return httpClient;
	}
	
	public static String post(HttpClient httpClient,String api,String params,String charset){
		System.out.println("xml==="+params);
		HttpPost post = null;
		try {
			String result = null;
			post = new HttpPost(api);
			post.setEntity(new StringEntity(params, charset));
			HttpResponse response = httpClient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if(entity!=null){
				result = EntityUtils.toString(entity,charset);
			}
			if (status == HttpStatus.SC_OK) {
				return result;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(post!=null)
				post.abort();
		}
		return null;
	}
}
