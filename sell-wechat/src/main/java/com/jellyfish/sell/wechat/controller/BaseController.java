package com.jellyfish.sell.wechat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

public class BaseController {
	/**
	 * 返回json数据
	 * @param response
	 * @param jsonString
	 */
	protected void writeJson(HttpServletResponse response, String jsonString){
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(jsonString);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	protected void writeXml(String result, HttpServletResponse response) {
		try {
			byte[] data = result.getBytes("utf-8");
			response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
			response.setContentLength(data.length);
			response.getOutputStream().write(data);
			response.flushBuffer();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void writeJSON(String result, HttpServletResponse response) {
		try {
			System.out.println("response=="+result);
			byte[] data = result.getBytes("utf-8");
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.setContentLength(data.length);
			response.getOutputStream().write(data);
			response.flushBuffer();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
