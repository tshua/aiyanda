 /**  
 *@Description:     
 */ 
package com.iyanda.common;  
  
  
public class ClassUtil {
	
	/**
	 * @param c
	 * @return
	 * @Author:lulei  
	 * @Description: 返回class文件所在的目录
	 */
	public static String getClassPath(Class<?> c) {
		return c.getResource("").getPath().replaceAll("%20", " ");
	}
	
	/**
	 * @param c
	 * @return
	 * @Author:lulei  
	 * @Description: 返回class文件所在项目的根目录
	 */
	public static String getClassRootPath(Class<?> c) {
		return c.getResource("/").getPath().replaceAll("%20", " ");
	}
	
	/**
	 * @param c
	 * @param hasName 是否包括class名
	 * @return
	 * @Author:lulei  
	 * @Description: 返回class文件所在的目录
	 */
	public static String getClassPath(Class<?> c, boolean hasName) {
		String name = c.getSimpleName() + ".class";
		String path = c.getResource(name).getPath().replaceAll("%20", " ");
		if (hasName) {
			return path;
		} else {
			return path.substring(0, path.length() - name.length());
		}
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
