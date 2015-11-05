package com.wechat.utils;

import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.wechat.message.resp.Article;
import com.wechat.message.resp.MusicMessage;
import com.wechat.message.resp.NewsMessage;
import com.wechat.message.resp.TextMessage;

public class XMLParserUtil {
	@SuppressWarnings("unchecked")
	public static Map<String,String> parseXml(HttpServletRequest request) throws Exception{
		 Map<String, String> map = new HashMap<String, String>();  
		  
		    // ��request��ȡ��������  
		    InputStream inputStream = request.getInputStream();  
		    // ��ȡ������  
		    SAXReader reader = new SAXReader();  
		    Document document = reader.read(inputStream);  
		    // �õ�xml��Ԫ��  
		    Element root = document.getRootElement();  
		    // �õ���Ԫ�ص������ӽڵ�  
		    List<Element> elementList = root.elements();  
		  
		    // ���������ӽڵ�  
		    for (Element e : elementList)  
		        map.put(e.getName(), e.getText());  
		  
		    // �ͷ���Դ  
		    inputStream.close();  
		    inputStream = null;  
		  
		    return map;  
	}
	
	
	/** 
	 * ��չxstream��ʹ��֧��CDATA�� 
	 *  
	 * @date 2013-05-19 
	 */  
	private static XStream xstream = new XStream(new XppDriver() {  
	    public HierarchicalStreamWriter createWriter(Writer out) {  
	        return new PrettyPrintWriter(out) {  
	            // ������xml�ڵ��ת��������CDATA���  
	            boolean cdata = true;  
	  
	            @SuppressWarnings("unchecked")  
	            public void startNode(String name, Class clazz) {  
	                super.startNode(name, clazz);  
	            }  
	  
	            protected void writeText(QuickWriter writer, String text) {  
	                if (cdata) {  
	                    writer.write("<![CDATA[");  
	                    writer.write(text);  
	                    writer.write("]]>");  
	                } else {  
	                    writer.write(text);  
	                }  
	            }  
	        };  
	    }  
	});  
	
	
	
	public static String textMessageToXml(TextMessage tm){
		xstream.alias("xml", tm.getClass());  
	    return xstream.toXML(tm);  
	}
	
	public static String musicMessageToXml(MusicMessage musicMessage){
		xstream.alias("xml", musicMessage.getClass());  
	    return xstream.toXML(musicMessage);  
	}
	
	public static String newsMessageToXml(NewsMessage newsMessage){
		xstream.alias("xml", newsMessage.getClass());  
	    xstream.alias("item", new Article().getClass());  
	    return xstream.toXML(newsMessage); 
	}
	
	
}
