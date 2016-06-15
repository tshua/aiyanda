package com.iyanda.crawljwc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.iyanda.common.CharsetUtil;
import com.iyanda.common.RegexUtil;

public class LoginCheck {

	private static Logger log = Logger.getLogger(LoginCheck.class);

	private static final String loginUrl = "http://jwc.ysu.edu.cn/zjdxgc/default2.aspx";

	private static final String checkCodeUrl = "http://jwc.ysu.edu.cn/zjdxgc/CheckCode.aspx";

	private static final int maxConnectTimes = 3;

	// 将HttpClient委托给MultiThreadedHttpConnectionManager，支持多线程
	private static MultiThreadedHttpConnectionManager httpConnectManager = new MultiThreadedHttpConnectionManager();
	private static HttpClient httpClient = new HttpClient(httpConnectManager);

	// 请求头信息
	private static HashMap<String, String> params;

	// 初始化消息头
	static {
		params = new HashMap<String, String>();
		params.put("Referer", "http://jwc.ysu.edu.cn/zjdxgc/default2.aspx");
		params.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
		params.put("Host", "jwc.ysu.edu.cn");
	}

	private static final String LoginError = "<script>alert\\(\\'(.*?)\\'\\);</script>";

	public String downloadCheckCode(String serverRealPath) {// 下载验证码
		GetMethod get = new GetMethod(checkCodeUrl);
		int n = maxConnectTimes;
		while (n > 0) {
			try {
				// 判断返回状态是否是200
				if ((httpClient.executeMethod(get) != 200)
						&& (httpClient.executeMethod(get) != 302)) {
					log.error("can`t connect " + loginUrl
							+ (maxConnectTimes - n + 1));
					n--;
				} else {

					System.out.println("成功返回验证码");

					//文件名称
					serverRealPath += "\\";
					String tfilename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "a.jpg"; 
                    String path = serverRealPath + tfilename;
					
					File outFile = new File(path);
					OutputStream os = new FileOutputStream(outFile);
					// 获取服务器的输出流
					InputStream inputStream = get.getResponseBodyAsStream();

					byte[] buff = new byte[1024];
					while (true) {
						int readed = inputStream.read(buff);
						if (readed == -1) {
							break;
						}
						byte[] temp = new byte[readed];
						System.arraycopy(buff, 0, temp, 0, readed); // 这句是关键
						os.write(temp);
					}

					os.close();
					inputStream.close();

					return tfilename;
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(loginUrl + "can`t connect "
						+ (maxConnectTimes - n + 1));
				n--;
			}

		}

		return "";
	}

	public String islogin(String xh, String pw, String checkCode) {// 判断用户名和密码

		PostMethod post = new PostMethod(loginUrl);
		NameValuePair __VIEWSTATE = new NameValuePair("__VIEWSTATE",
				"dDwyODE2NTM0OTg7Oz4RhZJnGhjM9FmCL3zGnGfgpyyQeA==");
		NameValuePair name = new NameValuePair("txtUserName", xh);
		NameValuePair pwd = new NameValuePair("TextBox2", pw);
		NameValuePair checkCode1 = new NameValuePair("txtSecretCode", checkCode);
		NameValuePair RadioButtonList1 = new NameValuePair("RadioButtonList1",
				"学生");
		NameValuePair Button1 = new NameValuePair("Button1", "");
		NameValuePair lbLanguage = new NameValuePair("lbLanguage", "");
		NameValuePair hidPdrs = new NameValuePair("hidPdrs", "");
		NameValuePair hidsc = new NameValuePair("hidsc", "");

		post.setRequestBody(new NameValuePair[] { __VIEWSTATE, name, pwd,
				checkCode1, RadioButtonList1, Button1, lbLanguage, hidPdrs,
				hidsc });

		int n = maxConnectTimes;
		while (n > 0) {
			try {
				// 判断返回状态是否是200
				int res = httpClient.executeMethod(post);
				// if ((httpClient.executeMethod(post) != 200) &&
				// (httpClient.executeMethod(post) != 302)) {
				if (res != 200 && res != 302) {
					log.error("can`t connect " + loginUrl
							+ (maxConnectTimes - n + 1));
					n--;
				} else {

					// 获取服务器的输出流
					InputStream inputStream = post.getResponseBodyAsStream();
					BufferedReader bufferReader = new BufferedReader(
							new InputStreamReader(inputStream, "iso-8859-1"));
					StringBuffer stringBuffer = new StringBuffer();
					String lineString = "";
					while ((lineString = bufferReader.readLine()) != null) {
						stringBuffer.append(lineString);
						stringBuffer.append("\n");
					}
					String pageSourceCode = stringBuffer.toString();
					// 检测流的编码方式
					InputStream in = new ByteArrayInputStream(
							pageSourceCode.getBytes("iso-8859-1"));
					String charset = CharsetUtil.getStreamCharset(in, "utf-8");
					// 如果编码方式不同，则进行转码操作
					if (!"iso-8859-1".toLowerCase().equals(
							charset.toLowerCase())) {
						pageSourceCode = new String(
								pageSourceCode.getBytes("iso-8859-1"), charset);
					}

					//System.out.println(pageSourceCode);

					// 判断是否成功
					/*
					 * <script>alert('密码错误！！');</script>
					 * <script>alert('用户名不存在或未按照要求参加教学活动！！');</script>
					 * 
					 * 
					 * 登陆成功：
					 * 
					 * /zjdxgc/xs_main.aspx?xh=
					 */
					if (pageSourceCode.contains("/zjdxgc/xs_main.aspx?xh=")) {
						// System.out.println("ok");
						return "ok";

					} else {
						return "loginerror";
						/*
						 * String res = RegexUtil.getFirstString(pageSourceCode,
						 * LoginError, 1); //System.out.println(res); return
						 * res;
						 */
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				log.error(loginUrl + "can`t connect "
						+ (maxConnectTimes - n + 1));
				n--;
			}
		}
		return "error";

	}

	/**
	 * @param args
	 * @Author:tianshaohua
	 * @Description:
	 */
	public static void main(String[] args) {
		LoginCheck lc = new LoginCheck();
		//lc.downloadCheckCode();
		// System.out.println(lc.islogin("130120010193", "zise..nianhua"));

	}

}
