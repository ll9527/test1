package com.service;

import com.entity.Version;

import java.util.List;

public interface VersionService {
    int deleteByPrimaryKey(Integer id);

    int insert(Version record);

    int insertSelective(Version record);

    Version selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Version record);

    int updateByPrimaryKey(Version record);

    List<Version> selectVersionByPid(Integer productid);
}
