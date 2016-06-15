 /**  
 *@Description:     
 */ 
package com.iyanda.common;  

import java.security.MessageDigest;
  
  
public class Encrypt {
	//MD5加密
	public static final String MD5 = "MD5";
	//SHA-1加密
	public static final String SHA1 = "SHA-1";
	//SHA-256加密
	public static final String SHA256 = "SHA-256";
	
	/**
	 * @param str
	 * @param encName 加密种类名
	 * @return
	 * @Author:lulei  
	 * @Description: 对字符串加密
	 */
	public static String encrypt(String str, String encName) {
		String reStr = null;
		try {
			//加密器
			MessageDigest digest = MessageDigest.getInstance(encName);
			byte[] bytes = digest.digest(str.getBytes());
			StringBuffer stringBuffer = new StringBuffer();
			for (byte b : bytes) {
				int bt = b&0xff;
				//如果小于16，补位一个0
				if (bt < 16) {
					stringBuffer.append(0);
				}
				stringBuffer.append(Integer.toHexString(bt));
			}
			
			reStr = stringBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reStr;
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
