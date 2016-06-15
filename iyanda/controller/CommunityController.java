package com.iyanda.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iyanda.common.JsonUtil;
import com.iyanda.common.PushUtil;
import com.iyanda.entity.CommunityData;
import com.iyanda.entity.CommunityEvent;
import com.iyanda.entity.CommunityInform;
import com.iyanda.entity.Rlation;
import com.iyanda.entity.User;
import com.iyanda.service.CommunityService;

@Controller
public class CommunityController {

	@Resource
	private CommunityService communityService;

	@RequestMapping(value = "createCommunity")
	public void createCommunity(String username,HttpServletRequest request, HttpServletResponse response) throws IOException{
		String communityName = request.getParameter("communityname");
		String communityIntro = request.getParameter("communityIntro");
		String type = request.getParameter("type");
		
		User user = communityService.getUserByName(username);

		
		
		CommunityData cd = new CommunityData();
		cd.setCommunityname(communityName);
		cd.setCommunityintro(communityIntro);
		cd.setType(type);
		response.getWriter().write(communityService.createCommunity(cd));
		
		
		//把创建人设为主席
		Rlation rlation = new Rlation();
		rlation.setPosition("主席");
		rlation.setUserid(user.getId());
		rlation.setCommunityid(cd.getId());
		
	}
	
	@RequestMapping(value="getUseridByName")
	public void getUseridByName(String username,HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.getWriter().write(communityService.getUserByName(username).getId());
	}
	
	
	@RequestMapping(value = "getCommunityList")
	public void getCommunityList(String username,HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<CommunityData> communitys = communityService.getCommunitysByUserName(username);
		response.getWriter().write(JsonUtil.toJson(communitys));
	}
	
	@RequestMapping(value="getEvents")//获取所有事件
	public void getEvents(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String communityid = request.getParameter("communityid");
		List<CommunityEvent> events = communityService.getEventByCommunityId(communityid);
		response.getWriter().write(JsonUtil.toJson(events));
	}
	
	
	@RequestMapping(value = "addEvent")//添加事件  传进event对象
	public void addEvent(HttpServletRequest request, HttpServletResponse response) throws IOException{
		CommunityEvent event = JsonUtil.toObject((String)request.getAttribute("event"), CommunityEvent.class);
		communityService.addEvent(event);
		response.getWriter().write("ok");
	}
	
	@RequestMapping(value="joinCommunity")//添加社团   传进rlation对象
	public void joinCommunity(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Rlation rlation = JsonUtil.toObject((String)request.getAttribute("rlation"), Rlation.class);
		communityService.joinCommunity(rlation);
		response.getWriter().write("ok");
	}
	
	@RequestMapping(value = "editData")//修改社团信息  传进communitydata对象
	public void editData(HttpServletRequest request, HttpServletResponse response) throws IOException{
		CommunityData cd = JsonUtil.toObject((String)request.getAttribute("communitydata"), CommunityData.class);
		communityService.editCommunity(cd);
		response.getWriter().write("ok");
	}
	
	@RequestMapping(value = "delEvent")//删除社团事件  传进event 的id
	public void delEvent(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String eventid = request.getParameter("eventid");
		communityService.delEventById(eventid);
		response.getWriter().write("ok");
	}
	
	@RequestMapping(value = "getFlieList")//获取文件列表
	public void getFLieList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String communityid = request.getParameter("communityid");
		response.getWriter().write(JsonUtil.toJson(communityService.getFileListByCommunityId(communityid)));
	}
	
	@RequestMapping(value = "getAllRlation")//获取社团关系
	public void getAllRlation(String communityid,HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().write(JsonUtil.toJson(communityService.getRlationsByCommunityId(communityid)));
	}
	@RequestMapping(value = "getUsersInCommunity")
	public void getUsersInCommunity(String communityid,HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().write(JsonUtil.toJson(communityService.getUsersInCommunity(communityid)));
	}
	
	@RequestMapping(value = "getRlation")//获取一个人的社团关系
	public void getRlation(String username,HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().write(JsonUtil.toJson(communityService.getRlationsByUserName(username)));
		
	}
	@RequestMapping(value = "editRlation")//任免部员
	public void editRlation(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Rlation rlation = JsonUtil.toObject((String)request.getAttribute("rlation"), Rlation.class);
		response.getWriter().write(communityService.editRlation(rlation));
	}
	
	@RequestMapping(value = "exitCommunity")//退出社团
	public void delRlation(String rlationid,HttpServletRequest request, HttpServletResponse response) throws IOException{
		communityService.exitCommunity(rlationid);
		response.getWriter().write("ok");
	}
	
	@RequestMapping(value = "pushCommunityInform")//推送社团消息
	public void pushCommunityInform(HttpServletRequest request, HttpServletResponse response) throws IOException, PushClientException, PushServerException{
		CommunityInform inform = JsonUtil.toObject((String)request.getAttribute("communityinform"),CommunityInform.class);
		//receiverid 是社团id
		PushUtil.pushToTag(inform.getReceiverid(), inform.getContent(), "");

		communityService.addCommunityInform(inform);
		
		response.getWriter().write("ok");
	}
	
	
	
}
