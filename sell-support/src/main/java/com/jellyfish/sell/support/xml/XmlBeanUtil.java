package com.jellyfish.sell.support.xml;

import com.thoughtworks.xstream.XStream;

/**
 * 该类仅用于微信相关的接口的XML解析
 * 因为重写了某些方法来适应微信的XML结构
 * 对其他的可能会造成未知错误
 * 其他的请使用common中的BeanXmlChangeUtil类
 * */
@SuppressWarnings("unchecked")
public class XmlBeanUtil {
	public static <T> T XMLToBean(String body,Class<T> clazz) {
		try{
			XStream xstream = new XStream(new MyXmlDriver());
			xstream.alias("xml", clazz);
			return (T) xstream.fromXML(body);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T XMLToBean(String body,Class<T> clazz,String rootElement) {
		try{
			XStream xstream = new XStream(new MyXmlDriver());
			xstream.alias(rootElement, clazz);
			return (T) xstream.fromXML(body);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static <T> String BeanToXML(T t,Class<T> clazz){
		XStream xstream = new XStream(new MyXmlDriver());
		xstream.alias("xml",clazz);
		String xml = xstream.toXML(t);
		return xml;
	}
}
