package com.iyanda.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iyanda.common.JsonUtil;
import com.iyanda.entity.LocalityData;
import com.iyanda.service.LocationService;

@Controller
public class LocationAnalysis {

	@Resource
	private LocationService locationService;
	
	
	@RequestMapping(value = "LocationAnalysis")
	public String LocationAnalysis(HttpServletRequest request, HttpServletResponse response) {
		return "LocationAnalysis";
	}
	
	@RequestMapping(value = "AnalysisResult")//test
	public String AnalysisResult(HttpServletRequest request, HttpServletResponse response) {
		return "AnalysisResult";
	}
	
	@RequestMapping(value = "pie")//test
	public String pie(HttpServletRequest request, HttpServletResponse response) {
		return "pie";
	}
	
	@RequestMapping(value = "resultPage")//test
	public String resultPage(HttpServletRequest request, HttpServletResponse response) {
		return "resultPage";
	}
	
	
	@RequestMapping(value = "uploadLocation")//上传位置信息
	public void uploadLocation(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LocalityData location = JsonUtil.toObject((String)request.getAttribute("localitydata"), LocalityData.class);
		locationService.addLocalityData(location);
		response.getWriter().write("ok");
	}

}
