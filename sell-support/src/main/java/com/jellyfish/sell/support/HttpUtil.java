package com.jellyfish.sell.support;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpUtil {
    public static JSONObject httpGetUrl(String url) {
        InputStream inputStream = null;
        CloseableHttpClient httpClient = null;
        HttpResponse response = null;
        try {
            httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(url);
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            String data = getStringContent(inputStream);
            return JSONObject.parseObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                close(inputStream, httpClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String getStringContent(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        byte[] bytes = bos.toByteArray();
        return new String(bytes, "utf-8");
    }


    private static void close(InputStream inputStream, CloseableHttpClient httpClient) throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
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
}
