/**  
 *@Description:     
 */
package com.iyanda.crawljwc;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.iyanda.crawl.CrawlBase;
import com.iyanda.common.RegexUtil;


@Repository
public class NoticePage extends CrawlBase {
	private String url;
	// 请求头信息
	private static HashMap<String, String> params;

	private static final String Notice = "<a href=\"notice_show.asp\\?id=(\\d{4})\" target=\"_blank\">(.*?)</a>";

	// 提取的内容在正则表达式中的位置
	private static final int[] array = { 1, 2 };

	// 初始化消息头
	static {
		params = new HashMap<String, String>();
		params.put("Referer", "http://jwc.ysu.edu.cn");
		params.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
		params.put("Host", "jwc.ysu.edu.cn");
	}
	
	public NoticePage() {
		readPageByGet("http://jwc.ysu.edu.cn/notice_index.asp", params, "utf-8");
		this.url = "http://jwc.ysu.edu.cn/notice_index.asp";
	}

	public NoticePage(String url) {
		readPageByGet(url, params, "utf-8");
		this.url = url;
	}

	public List<String[]> getNotice() {

		return RegexUtil.getList(getPageSourceCode(), Notice, array);
	}

	/**
	 * @param args
	 * @Author:tianshaohua
	 * @Description:
	 */
	public static void main(String[] args) {

		NoticePage introPage = new NoticePage(
				"http://jwc.ysu.edu.cn/notice_index.asp");

		for (String[] ss : introPage.getNotice()) {
			for (String s : ss) {
				System.out.println(s);
			}
			// System.out.println(introPage.getNotice());
		}

	}

}
