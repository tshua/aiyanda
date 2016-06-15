/**  
 *@Description:     
 */
package com.iyanda.crawljwc;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.iyanda.crawl.CrawlBase;
import com.iyanda.common.RegexUtil;



public class NoticeShowPage extends CrawlBase {
	private String url;
	// 请求头信息
	private static HashMap<String, String> params;

	// private static final String Notice =
	// "<a href=\"notice_show.asp\\?id=(\\d{4})\" target=\"_blank\">(.*?)</a>";

	private static final String NoticeTitle = "<tr height=\"70\"><td width=\"100%\" align=\"center\" valign=\"bottom\"><font size=4><b>(.*?)</b></font><br><font color=\"#666666\">(.*?)<br>(.*?\\[\\d{1,10}\\].)</font></td></tr>";
	private static final String NoticContent = "<SPAN id=BodyLabel style=\"line-height: 150%; PADDING-RIGHT: 10px; DISPLAY: block; PADDING-LEFT: 10px; PADDING-BOTTOM: 0px; PADDING-TOP: 0px\">(.*?)</span>"; 
	private static final String AdditionalFlie = "<tr height=\"40\"><td>.(.*?)</td></tr>";
	// 提取的内容在正则表达式中的位置
	private static final int[] array = { 1, 2, 3 };

	// 初始化消息头
	static {
		params = new HashMap<String, String>();
		params.put("Referer", "http://jwc.ysu.edu.cn");
		params.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
		params.put("Host", "jwc.ysu.edu.cn");
	}

	public NoticeShowPage(String url) {
		readPageByGet(url, params, "utf-8");
		this.url = url;
	}

	public String[] getNotice() {
		return RegexUtil.getFirstArray(getPageSourceCode(), NoticeTitle, array);
	}
	
	public String getNoticeContent(){
		return RegexUtil.getFirstString(getPageSourceCode(), NoticContent, 1);
	}
	public String getAdditionalFlie(){
		return RegexUtil.getFirstString(getPageSourceCode(), AdditionalFlie, 1);
	}

	/**
	 * @param args
	 * @Author:tianshaohua
	 * @Description:
	 */
	public static void main(String[] args) {

		NoticeShowPage noticeShowPage = new NoticeShowPage(
				"http://jwc.ysu.edu.cn/notice_show.asp?id=1279");

		for (String ss : noticeShowPage.getNotice()) {
			System.out.println(ss);
		}
		
		System.out.println(noticeShowPage.getNoticeContent());
		
		System.out.println(noticeShowPage.getAdditionalFlie());
		
		// System.out.println(introPage.getNotice());

	}

}
