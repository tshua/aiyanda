package com.iyanda.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.iyanda.crawljwc.LoginCheck;
import com.iyanda.entity.User;
import com.iyanda.service.UserService;

@Controller
public class LoginController {
	@Resource
	private UserService userService;

	LoginCheck lc = new LoginCheck();

	@RequestMapping(value = "login")
	public void login(String username, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.getWriter().write(
					JsonUtil.toJson(userService.updateStatus(username)));

		} catch (IOException e) {
		}
	}

	@RequestMapping(value = "logout")
	public void logout(String username, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.getWriter().write(
					JsonUtil.toJson(userService.updateStatusout(username)));

		} catch (IOException e) {
		}
	}

	@RequestMapping(value = "getUserByid")
	public void getUserByid(String userid, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		User user = userService.getUserByid(userid);
		response.getWriter().write(JsonUtil.toJson(user));
	}

	@RequestMapping(value = "getUserByName")
	public ModelAndView getUserByName(String username,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		User user = userService.getUserByUserName(username);
		List<User> users = new ArrayList<>();
		users.add(user);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("users", users);

		return new ModelAndView("userList", model);
	}

	@RequestMapping(value = "getCheckCode")
	public void getCheckCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String serverRealPath = request.getSession().getServletContext()
				.getRealPath("/upload");
		String res = lc.downloadCheckCode(serverRealPath);//下载到服务器
		
		//下载到手机
		if ("".equals(res)) {
			response.getWriter().write("获取验证码失败");
		} else {

			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName="
					+ res);
			try {
				String path = request.getSession().getServletContext()
						.getRealPath("/upload");
				path += "\\";
				InputStream inputStream = new FileInputStream(new File(path
						+ File.separator + res));
				OutputStream os = response.getOutputStream();
				byte[] b = new byte[2048];
				int length;
				while ((length = inputStream.read(b)) > 0) {
					os.write(b, 0, length);
				}
				// 这里主要关闭。
				os.close();
				inputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 返回值要注意，要不然就出现下面这句错误！
			// java+getOutputStream() has already been called for this response

		}
	}

	@RequestMapping(value = "regist")
	public void regist(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String res = lc.islogin(request.getParameter("username"),
				request.getParameter("password"),
				request.getParameter("checkcode"));
		if (res.equals("ok")) {

			if (userService.getUserByUserName(request.getParameter("username")) != null) {
				response.getWriter().write("exist");
			} else {
				response.getWriter().write("ok");
				User user = new User();
				user.setUsername(request.getParameter("username"));
				user.setUsernickname(request.getParameter("tel"));
				user.setPassword(request.getParameter("password"));
				user.setPersonalsign(0);
				user.setImagesrc("0");// 普通用户
				userService.addUser(user);
			}
		} else {
			response.getWriter().write(res);
		}
	}

	@RequestMapping(value = "adminSystem")
	public String adminSystem() {
		return "lrp_login";
	}

	@RequestMapping(value = "adminLogin")
	public ModelAndView adminLogin(String username, String password,
			HttpServletRequest request, HttpServletResponse response) {

		/*
		 * System.out.println(username); System.out.println(password);
		 */
		User user = userService.getUserByUsernameAndPass(username, password);

		Map<String, Object> model = new HashMap<String, Object>();

		if (user == null)// 登陆失败
		{
			model.put("loginSign", "F");
			return new ModelAndView("lrp_login", model);
		} else {// 成功

			request.getSession().setAttribute("user", user);

			return new ModelAndView("lrp_systemHome", model);
		}
	}
}
