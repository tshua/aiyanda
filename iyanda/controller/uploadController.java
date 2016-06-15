package com.iyanda.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.iyanda.entity.Doc;
import com.iyanda.service.CommunityService;

@Controller
public class uploadController {

	private static String communityid;
	//private static String permissionnum;

	@Resource
	private CommunityService communityService;

	@RequestMapping(value = "upload")
	public String upload(String communityid){//, String permissionnum) {
		this.communityid = communityid;
		//this.permissionnum = permissionnum;//0部长  1所有
		return "fileUpload";
	}

	@RequestMapping(value = "upload2")
	public void upLoad2(String permissionnum,HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException {
		// 解析器解析request的上下文
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 先判断request中是否包涵multipart类型的数据，
		if (multipartResolver.isMultipart(request)) {
			// 再将request中的数据转化成multipart类型的数据
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {
					String fileName = file.getOriginalFilename();
                    //  下面的加的日期是为了防止上传的名字一样

                    
					String serverRealPath = request.getSession()
							.getServletContext().getRealPath("/upload");
					serverRealPath += "\\";
					String tfilename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + fileName; 
                    String path = serverRealPath + tfilename;
					File localFile = new File(path);
					// 写文件到本地
					file.transferTo(localFile);
					
					
					// 文件信息写到数据库
					Doc doc = new Doc();
					doc.setCommunityid(communityid);
					doc.setPermissionnum(Integer.valueOf(permissionnum));
					doc.setDate(new Date());
					doc.setDocumentname(fileName);
					doc.setDocumentsrc(tfilename);

					communityService.addDoc(doc);
				}
			}
		}

		response.getWriter().write("上传成功");
	}

	@RequestMapping(value = "test")
	public void test(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String serverRealPath = request.getSession().getServletContext()
				.getRealPath("/upload");
		response.getWriter().write(serverRealPath);
	}

	@RequestMapping("download")
	public String download(String fileid, HttpServletRequest request,
			HttpServletResponse response) {
		
		Doc doc = communityService.getDocByFileId(fileid);
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName="
				+ doc.getDocumentname());
		try {
			String path = request.getSession().getServletContext()
					.getRealPath("/upload");
			path += "\\";
			InputStream inputStream = new FileInputStream(new File(path
					+ File.separator + doc.getDocumentsrc()));
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
		return null;
	}
}
