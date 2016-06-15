package com.iyanda.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iyanda.dao.CommunityDataMapper;
import com.iyanda.dao.CommunityEventMapper;
import com.iyanda.dao.CommunityInformMapper;
import com.iyanda.dao.DocMapper;
import com.iyanda.dao.RlationMapper;
import com.iyanda.dao.UserMapper;
import com.iyanda.entity.CommunityData;
import com.iyanda.entity.CommunityEvent;
import com.iyanda.entity.CommunityInform;
import com.iyanda.entity.Doc;
import com.iyanda.entity.Rlation;
import com.iyanda.entity.User;

@Service
public class CommunityService {

	@Resource
	private CommunityDataMapper communityDataDao;
	@Resource
	private CommunityEventMapper communityEventDao;
	@Resource
	private RlationMapper rlationDao;
	@Resource
	private DocMapper docDao;
	@Resource
	private UserMapper userDao;
	@Resource
	private CommunityInformMapper informDao;

	public String createCommunity(CommunityData communityData) {
		if (communityDataDao.selectByName(communityData.getCommunityname()) == null) {
			communityDataDao.insert(communityData);
			return "ok";
		} else {
			return "exists";
		}
	}

	public List<CommunityData> getCommunitysByUserName(String username) {// 根据用户获取所有的社团
		User user = userDao.selectByUserName(username);
		List<Rlation> rlations = rlationDao.selectByUserId(user.getId());
		List<CommunityData> cds = new ArrayList<CommunityData>();
		for (Rlation r : rlations) {
			CommunityData cd = communityDataDao.selectByPrimaryKey(r
					.getCommunityid());
			cds.add(cd);
		}
		return cds;
	}

	public int addEvent(CommunityEvent communityEvent) {
		return communityEventDao.insert(communityEvent);
	}

	public int delEventById(String id) {
		return communityEventDao.deleteByPrimaryKey(id);
	}

	public List<CommunityEvent> selectAllEvent() {
		return communityEventDao.selectAll();
	}

	public List<CommunityEvent> getEventByCommunityId(String communityId) {
		return communityEventDao.selectEventByCommunityId(communityId);
	}

	public int joinCommunity(Rlation rlation) {
		return rlationDao.insert(rlation);
	}

	public int editCommunity(CommunityData communityData) {
		return communityDataDao.updateByPrimaryKey(communityData);
	}

	public String editRlation(Rlation rlation) {
		Rlation r = rlationDao.selectByCommunityIdAndUserId(
				rlation.getCommunityid(), rlation.getUserid());
		if (r != null)// 已存在该关系
		{
			r.setPosition(rlation.getPosition());
			rlationDao.updateByPrimaryKey(r);
		} else {
			rlationDao.insert(rlation);
		}
		return "ok";
	}

	public List<Doc> getFileListByCommunityId(String id) {
		return docDao.selectByCommunityId(id);
	}

	public int addDoc(Doc doc) {
		return docDao.insert(doc);
	}

	public int delDocById(String docid) {
		return docDao.deleteByPrimaryKey(docid);
	}

	public Doc getDocByDocTname(String doctname) {
		return docDao.selectByTname(doctname);
	}

	public List<Rlation> getRlationsByCommunityId(String communityId) {
		return rlationDao.selectByCommunityId(communityId);
	}

	public int exitCommunity(String rlationId) {
		return rlationDao.deleteByPrimaryKey(rlationId);
	}

	public List<Rlation> getRlationsByUserName(String username) {
		User user = userDao.selectByUserName(username);
		return rlationDao.selectByUserId(user.getId());
	}

	public Doc getDocByFileId(String fileid) {
		return docDao.selectByPrimaryKey(fileid);
	}

	public int addCommunityInform(CommunityInform inform) {
		return informDao.insert(inform);
	}

	public List<User> getUsersInCommunity(String communityid) {
		List<Rlation> rlations = rlationDao.selectByCommunityId(communityid);
		List<User> users = new ArrayList<User>();
		for (Rlation r : rlations) {
			User u = userDao.selectByPrimaryKey(r.getUserid());
			users.add(u);
		}
		return users;
	}
	
	public List<CommunityData> getAllCommunity()
	{
		return communityDataDao.selectAll();
	}

	public CommunityData getCommunityById(String communityid) {
		// TODO Auto-generated method stub
		return communityDataDao.selectByPrimaryKey(communityid);
	}

	public List<Doc> getAllDocs() {
		// TODO Auto-generated method stub
		return docDao.selectAll();
	}

	public User getUserByName(String username) {
		return userDao.selectByUserName(username);
	}

}
