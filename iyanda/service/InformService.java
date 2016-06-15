package com.iyanda.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.iyanda.common.PushUtil;
import com.iyanda.crawljwc.NoticePage;
import com.iyanda.dao.InformMapper;
import com.iyanda.entity.Inform;

@Service
public class InformService {

	
	@Resource
	private NoticePage noticPage;
	
	@Resource
	private InformMapper informDao;
	
	public int updateData() throws PushClientException, PushServerException{//从教务系统爬取最新的通知
			
		for (String[] ss : noticPage.getNotice()) {

			Inform inform = informDao.selectByPrimaryKey(ss[0]);
			if(inform==null){
				Inform i = new Inform();
				i.setId(ss[0]);
				i.setContent("");
				i.setTitle(ss[1]);
				informDao.insert(i);
				
				
				//推送出新消息
				
				PushUtil.pushToAll(ss[1], "");
			}
		}
		
		
		
		
		return 1;
	}
	
	public List<Inform> getNoticeList() {
		return informDao.selectLimit15();
	}

	public List<Inform> getAllNotice() {
		return informDao.selectAll();
	}
	
}
