 /**  
 *@Description:  MD5加密算法  
 */ 
package com.iyanda.common;  
  
  
public class ParseMD5 extends Encrypt{
	
	/**
	 * @param str
	 * @return
	 * @Author:lulei  
	 * @Description: 32位小写MD5
	 */
	public static String parseStrToMD5 (String str) {
		return encrypt(str, MD5);
	}
	
	/**
	 * @param str
	 * @return
	 * @Author:lulei  
	 * @Description: 32位大写MD5
	 */
	public static String parseStrToUpperMD5 (String str) {
		return parseStrToMD5(str).toUpperCase();
	}
	
	/**
	 * @param str
	 * @return
	 * @Author:lulei  
	 * @Description: 16位小写MD5
	 */
	public static String parseStrTo16MD5 (String str) {
		return parseStrToMD5(str).substring(8, 24);
	}
	
	/**
	 * @param str
	 * @return
	 * @Author:lulei  
	 * @Description: 16位大写MD5
	 */
	public static String parseStrToUpper16MD5 (String str) {
		return parseStrToUpperMD5(str).substring(8, 24);
	}

	/**  
	 * @param args
	 * @Author:lulei  
	 * @Description:  
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub  
		System.out.println(ParseMD5.parseStrToMD5("abc"));
		System.out.println(ParseMD5.parseStrTo16MD5("abc"));

	}

}
