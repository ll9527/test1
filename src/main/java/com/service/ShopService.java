package com.service;

import com.entity.Shop;

import java.util.List;

public interface ShopService {
    int deleteByPrimaryKey(Integer classId);

    int insert(Shop record);

    int insertSelective(Shop record);

    Shop selectByPrimaryKey(Integer classId);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);

    List<Shop> selectAll();

    //查询所有目录
    List<Shop> selectLevel();

    //查询二级目录
    List<Shop> selectTwo();

    //根据classid查询目录
    List<Shop> selectLevel1and2(Integer classid);
}
