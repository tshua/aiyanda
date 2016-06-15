package com.iyanda.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class IMDemo {
	@RequestMapping(value = "IMDemo")
	public String hello(HttpServletRequest request,HttpServletResponse response){
		return "IMDEMO";
	}
}
