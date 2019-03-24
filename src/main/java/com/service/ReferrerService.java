package com.service;

import com.entity.Referrer;

public interface ReferrerService {
    int deleteByPrimaryKey(Integer id);

    int insert(Referrer record);

    int insertSelective(Referrer record);

    Referrer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Referrer record);

    int updateByPrimaryKey(Referrer record);
}
