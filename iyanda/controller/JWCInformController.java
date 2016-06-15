package com.iyanda.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.iyanda.common.JsonUtil;
import com.iyanda.crawljwc.NoticePage;
import com.iyanda.crawljwc.NoticeShowPage;
import com.iyanda.service.InformService;

@Controller
public class JWCInformController {

	@Resource
	private InformService informService;

	@RequestMapping(value = "getJWCList")
	public void getJWCList(HttpServletRequest request,
			HttpServletResponse response) throws IOException, PushClientException, PushServerException {
		informService.updateData();// 在每次查询的时候爬取数据更新
		response.getWriter().write(
				JsonUtil.toJson(informService.getNoticeList()));
	}

	
	@RequestMapping(value = "getJWCContent")
	public ModelAndView getJWCContent(String id, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		NoticeShowPage noticeShowPage = new NoticeShowPage(
				"http://jwc.ysu.edu.cn/notice_show.asp?id=" + id);
		/*
		 * System.out.println(s[0]);
		 * 
		 * System.out.println(noticeShowPage.getNoticeContent());
		 * 
		 * System.out.println(noticeShowPage.getAdditionalFlie());
		 */

		Map<String, Object> model = new HashMap<String, Object>();
		String res = "";
		response.setCharacterEncoding("gbk");
		for (String ss : noticeShowPage.getNotice()) {
			// response.getWriter().write(ss);
			// response.getWriter().write("<br>");
			res += ss;
		}
		// response.getWriter().write(noticeShowPage.getNoticeContent());
		// response.getWriter().write(noticeShowPage.getAdditionalFlie());
		res += noticeShowPage.getNoticeContent();

		String accessory = noticeShowPage.getAdditionalFlie();
		int fromIndex = 0;
		while (true) {
			int i = accessory.indexOf("admin", fromIndex);
			if (i >= 0) {
				accessory = accessory.substring(0, i)
						+ "http://jwc.ysu.edu.cn/"
						+ accessory.substring(i, accessory.length());
				fromIndex = i + 23;
			}
			else{
				break;
			}
		}
		
		res += accessory;

		model.put("res", res);
		return new ModelAndView("noticShow", model);
	}
	

}
