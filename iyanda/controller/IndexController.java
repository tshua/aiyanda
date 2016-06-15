package com.iyanda.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.iyanda.common.JsonUtil;
import com.iyanda.common.PageParam;
import com.iyanda.entity.CommunityData;
import com.iyanda.entity.Doc;
import com.iyanda.entity.Inform;
import com.iyanda.entity.Rlation;
import com.iyanda.entity.User;
import com.iyanda.service.CommunityService;
import com.iyanda.service.InformService;
import com.iyanda.service.TableIpService;
import com.iyanda.service.UserService;

@Controller
public class IndexController {

	@Resource
	TableIpService service;
	
	@Resource
	private UserService userService;
	
	@Resource CommunityService communityService;
	
	@Resource
	private InformService informService;
	
	@RequestMapping(value = "index")
	public String helloWorld(HttpServletRequest request){
		String currPageStr = request.getParameter("page");
		int currPage = 1;
		try {
			currPage = Integer.parseInt(currPageStr);
		} catch (Exception e) {
		}
		
		// 获取总记录数
		int rowCount = service.getRowCount();
		PageParam pageParam = new PageParam();
		pageParam.setRowCount(rowCount);
		if (pageParam.getTotalPage() < currPage) {
			currPage = pageParam.getTotalPage();
		}
		pageParam.setCurrPage(currPage);
		pageParam = service.getIpListByPage(pageParam);
		
		request.setAttribute("pageParam", pageParam);
		
		return "index";
	}
	
	@RequestMapping(value = "welcome")
	public String welcome(){
		return "welcom";
	}
	
	@RequestMapping(value = "userList")
	public ModelAndView userList(){
		List<User> users = userService.getAll();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("users", users);
		
		return new ModelAndView("userList",model);
	}
	
	
	@RequestMapping(value = "communityList")
	public ModelAndView communityList()
	{
		List<CommunityData> cds = communityService.getAllCommunity();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("cds", cds);
		
		return new ModelAndView("communityList",model);
	}
	
	
	@RequestMapping(value = "communityFileList")
	public ModelAndView communityFileList()
	{
		List<Doc> docs = communityService.getAllDocs();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("docs", docs);
		List<CommunityData> communitys = new ArrayList<>();
		for(Doc d:docs){
			communitys.add(communityService.getCommunityById(d.getCommunityid()));
		}
		model.put("communitys", communitys);
		
		return new ModelAndView("communityFileList",model);
	}
	
	
	
	@RequestMapping(value = "JWCNoticList")
	public ModelAndView JWCNoticList()
	{
		List<Inform> informs = informService.getAllNotice();
		Map<String,Object> model = new HashMap<String,Object>();
		
		model.put("informs", informs);
		
		return new ModelAndView("JWCNoticList",model);
	}

	@RequestMapping(value = "location")
	public String location()
	{
		return "location";
	}
	
	@RequestMapping(value = "deleteUser")
	public ModelAndView deleteUser(String userid){
		userService.delUser(userid);
		List<User> users = userService.getAll();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("users", users);
		
		return new ModelAndView("userList",model);
	}
	
	@RequestMapping(value = "deleteCUser")
	public ModelAndView deleteCUser(String userid,String communityid){
		List<Rlation> rs1 = communityService.getRlationsByCommunityId(communityid);
		for(Rlation r:rs1){
			if(r.getUserid().equals(userid)){
				communityService.exitCommunity(r.getId());
 			}
		}
		
		
		//返回
		List<User> users = communityService.getUsersInCommunity(communityid);
		List<Rlation> rs = communityService.getRlationsByCommunityId(communityid);
		CommunityData cd = communityService.getCommunityById(communityid);
		List<Rlation> rlations = new ArrayList<>();
		for(User u:users){
			for(Rlation r:rs){
				if(r.getUserid().equals(u.getId())){
					rlations.add(r);
				}
			}
		}
		
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("users", users);
		model.put("rlations",rlations);
		
		model.put("communityname", cd.getCommunityname());
		model.put("communityid", cd.getId());
		
		return new ModelAndView("cuserList",model);

	}
	
	@RequestMapping(value = "getFlies")//获取文件列表
	public ModelAndView getFlies(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String communityid = request.getParameter("communityid");
		List<Doc> docs = communityService.getFileListByCommunityId(communityid);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("docs", docs);
		CommunityData cd = communityService.getCommunityById(communityid);
		model.put("communityname", cd.getCommunityname());
		model.put("communityid", cd.getId());
		return new ModelAndView("flieList",model);
	}

	@RequestMapping(value = "deleteFile")//获取文件列表
	public ModelAndView deleteFile(String fileid,HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		//删除
		Doc doc = communityService.getDocByFileId(fileid);
		communityService.delDocById(fileid);
		
		String path = request.getSession().getServletContext()
				.getRealPath("/upload");
		path += "\\" + doc.getDocumentsrc();
		File file = new File(path);
		file.delete();
		
		//返回
		String communityid = request.getParameter("communityid");
		List<Doc> docs = communityService.getFileListByCommunityId(communityid);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("docs", docs);
		CommunityData cd = communityService.getCommunityById(communityid);
		model.put("communityname", cd.getCommunityname());
		model.put("communityid", cd.getId());
		
		if(communityid!=null)
			return new ModelAndView("flieList",model);
		else
			return new ModelAndView("communityFileList",model);
	}
	
	
	@RequestMapping(value = "deleteFileA")//获取文件列表
	public ModelAndView deleteFileA(String fileid,HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		//删除
		Doc doc = communityService.getDocByFileId(fileid);
		communityService.delDocById(fileid);
		
		String path = request.getSession().getServletContext()
				.getRealPath("/upload");
		path += "\\" + doc.getDocumentsrc();
		File file = new File(path);
		file.delete();
		
		//返回
		
		List<Doc> docs = communityService.getAllDocs();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("docs", docs);
		List<CommunityData> communitys = new ArrayList<>();
		for(Doc d:docs){
			communitys.add(communityService.getCommunityById(d.getCommunityid()));
		}
		model.put("communitys", communitys);
		
		return new ModelAndView("communityFileList",model);

		
	
		

	
	}
	
	
	@RequestMapping(value = "getUsers")//获取在某一社团的所有的用户
	public ModelAndView getUsers(String communityid,HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<User> users = communityService.getUsersInCommunity(communityid);
		List<Rlation> rs = communityService.getRlationsByCommunityId(communityid);
		CommunityData cd = communityService.getCommunityById(communityid);
		List<Rlation> rlations = new ArrayList<>();
		for(User u:users){
			for(Rlation r:rs){
				if(r.getUserid().equals(u.getId())){
					rlations.add(r);
				}
			}
		}
		
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("users", users);
		model.put("rlations",rlations);
		
		model.put("communityname", cd.getCommunityname());
		model.put("communityid", cd.getId());
		
		return new ModelAndView("cuserList",model);
	}
	
	@RequestMapping(value = "getCUserByName")
	public ModelAndView getCUserByName(String communityid,String username,HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<User> users1 = communityService.getUsersInCommunity(communityid);
		List<User> users = new ArrayList<>();
		for(User u:users1){
			if(u.getUsername().equals(username)){
				users.add(u);
			}
		}
		


		Map<String, Object> model = new HashMap<String, Object>();
		model.put("users", users);
		
		return new ModelAndView("userList",model);
	}
	
}
