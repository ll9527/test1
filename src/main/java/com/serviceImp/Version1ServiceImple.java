package com.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.Version1Mapper;
import com.entity.Version1;
import com.service.Version1Service;

@Service
public class Version1ServiceImple implements Version1Service{

	@Autowired(required = false)
	private Version1Mapper v1m;
	
	@Override
	public int insertSelective(Version1 version1) {
		return v1m.insertSelective(version1);
	}

}
