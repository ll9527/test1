package com.dao;

import com.entity.Shop;

import java.util.List;

public interface ShopMapper {
    int deleteByPrimaryKey(Integer classId);

    int insert(Shop record);

    int insertSelective(Shop record);

    Shop selectByPrimaryKey(Integer classId);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);

    List<Shop> selectAll();

    List<Shop> selectLevel();

    List<Shop> selectTwo();

    List<Shop> selectLevel1and2(Integer classid);
}