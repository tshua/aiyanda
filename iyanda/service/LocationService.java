package com.iyanda.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iyanda.dao.LocalityDataMapper;
import com.iyanda.entity.LocalityData;


@Service
public class LocationService {
	
	@Resource
	private LocalityDataMapper localityDao;

	public int addLocalityData(LocalityData location){
		return localityDao.insert(location);
	}
	
}
