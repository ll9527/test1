package com.dao;

import com.entity.Version;

import java.util.List;

public interface VersionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Version record);

    int insertSelective(Version record);

    Version selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Version record);

    int updateByPrimaryKey(Version record);

    List<Version>  selectVersionByPid(Integer productid);
}