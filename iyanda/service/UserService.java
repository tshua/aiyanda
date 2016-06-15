package com.iyanda.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iyanda.dao.UserMapper;
import com.iyanda.entity.User;


@Service
public class UserService {
	@Resource
	private UserMapper userDao;
	
	public User updateStatus(String username)
	{
		User user = userDao.selectByUserName(username);
		user.setPersonalsign(1);
		userDao.updateByPrimaryKey(user);
		return user;
	}
	
	public void addUser(User user){
		userDao.insert(user);
	}
	
	public User getUserByUserName(String username){
		return userDao.selectByUserName(username);
	}
	
	public User getUserByUsernameAndPass(String username,String password){
		return userDao.selectByUserNameAndPass(username,password);
	}

	public User getUserByid(String userid) {
		return userDao.selectByPrimaryKey(userid);
	}
	
	public List<User> getAll(){
		return userDao.selectAll();
	}
	
	public int delUser(String userid) {
		return userDao.deleteByPrimaryKey(userid);
	}

	public User updateStatusout(String username) {
		User user = userDao.selectByUserName(username);
		user.setPersonalsign(0);
		userDao.updateByPrimaryKey(user);
		return user;
	}

}
