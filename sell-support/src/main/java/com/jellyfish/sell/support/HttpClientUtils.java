package com.jellyfish.sell.support;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientUtils {

	// private static PoolingHttpClientConnectionManager poolConnManager = null;

	// private static CloseableHttpClient httpClient;
	// // 请求器的配置
	// private static RequestConfig requestConfig;

	public static PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager(int maxTotal,
			int maxPerRoute) {
		System.out.println("初始化HttpClientUtils~~~开始");
		try {
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
			// 配置同时支持 HTTP 和 HTPPS
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
			// 初始化连接管理器
			PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			// 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
			poolConnManager.setMaxTotal(maxTotal);
			// 设置最大路由
			poolConnManager.setDefaultMaxPerRoute(maxPerRoute);
			return poolConnManager;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static RequestConfig getRequestConfig(int connectionRequestTimeout, int socketTimeout, int connectTimeout) {
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
				.setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
		return requestConfig;
	}
	
	public static RequestConfig getRequestConfig(int connectionRequestTimeout, int socketTimeout, int connectTimeout,boolean isRedicect) {
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
				.setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).setRedirectsEnabled(false).build();
		return requestConfig;
	}

	public static CloseableHttpClient getConnection(PoolingHttpClientConnectionManager poolConnManager,
			RequestConfig requestConfig) {
		CloseableHttpClient httpClient = HttpClients.custom()
				// 设置连接池管理
				.setConnectionManager(poolConnManager)
				// 设置请求配置
				.setDefaultRequestConfig(requestConfig)
				// 设置重试次数
				.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();

		if (poolConnManager != null && poolConnManager.getTotalStats() != null) {
			System.out.println("now client pool " + poolConnManager.getTotalStats().toString());
		}

		return httpClient;
	}

	public static String httpGet(String url, CloseableHttpClient httpClient) {
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		String result = null;
		try {
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "utf-8");
				EntityUtils.consume(entity);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String httpPost(String url, String body, CloseableHttpClient httpClient) {
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse response = null;
		String result = null;
		try {

			StringEntity inEntity = new StringEntity(body, "utf-8");// 解决中文乱码问题
			httpPost.setEntity(inEntity);
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "utf-8");
				EntityUtils.consume(entity);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		
		CloseableHttpClient httpClient = getConnection(getPoolingHttpClientConnectionManager(200,200),getRequestConfig(1000, 1000, 1000,false));
		String url = "https://aweme.snssdk.com/aweme/v1/play/?video_id=v0300f0f0000bdogephsm3io1ivadk2g&line=0&ratio=720p&watermark=1&media_type=4&vr_type=0&test_cdn=None&improve_bitrate=0&logo_name=aweme";
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		String result = null;
		String location = null;
//		httpClient.get
//		org.apache.http.params.HttpParams params = new BasicHttpParams();
//        params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
//        httpGet.setParams(params);
		try {
			response = httpClient.execute(httpGet);
			System.out.println(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 302) {
				 org.apache.http.Header locationHeader = response.getFirstHeader("Location");
	             if(locationHeader!=null){
	                    location = locationHeader.getValue();
	                    System.out.println(location);
	             }
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
