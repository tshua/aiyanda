 /**  
 *@Description:     
 */ 
package com.iyanda.common;  

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
  
  
public class XmlUtil {
	private static final String noResult = "<result>no result</result>";
	
	/**
	 * @param xml
	 * @return
	 * @Author:lulei  
	 * @Description: 将java对象转化为xml格式的字符串
	 */
	public static Document createFromString(String xml) {
		try {
			return DocumentHelper.parseText(xml);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param object
	 * @return
	 * @Author:lulei  
	 * @Description: 将xml String对象转化为xml对象
	 */
	public static  String parseObject2XmlString (Object object) {
		if (object == null) {
			return noResult;
		}
		StringWriter sw = new StringWriter();
		JAXBContext jAXBContent;
		Marshaller marshaller;
		try {
			jAXBContent = JAXBContext.newInstance(object.getClass());
			marshaller = jAXBContent.createMarshaller();
			marshaller.marshal(object, sw);
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return noResult;
		}
	}
	
	/**
	 * @param xpath
	 * @param node
	 * @return
	 * @Author: lulei  
	 * @Description: 获取指定xpath的文本，当解析失败返回null
	 */
	public static String getTextFromNode(String xpath,Node node){
		try {
			return node.selectSingleNode(xpath).getText();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @param path
	 * @Author: lulei  
	 * @Description: 读取xml文件
	 * @return xml文件对应的Document
	 */
	public static Document createFromPath(String path){
		return createFromString(readFile(path));
	}
	
	/**
	 * @param path
	 * @Author: lulei  
	 * @Description: 读文件
	 * @return 返回文件内容字符串
	 */
	private static String readFile(String path) {
		File file = new File(path);
		FileInputStream fileInputStream;
		StringBuffer sb = new StringBuffer();
		try {
			fileInputStream = new FileInputStream(file);
			//错误使用UTF-8读取内容
			String charset = CharsetUtil.getStreamCharset(file.toURI().toURL(), "utf-8");
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String s;
			while ((s = bufferedReader.readLine()) != null){
				s = s.replaceAll("\t", "").trim();
				if (s.length() > 0){
					sb.append(s);
				}
			}
			fileInputStream.close();
			bufferedReader.close();
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block  
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block  
			e.printStackTrace();
		} 
		return sb.toString();
	}

	/**  
	 * @param args
	 * @Author:lulei  
	 * @Description:  
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub  

	}

}
