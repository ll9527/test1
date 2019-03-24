package com.dao;

import java.util.List;

import com.entity.Version1;

public interface Version1Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Version1 record);

    int insertSelective(Version1 record);

    Version1 selectByPrimaryKey(Integer id);
    
    /**
     * 通过商品id查询该商品的型号（颜色）
     * @param productid
     * @return
     */
    List<Version1> selectByProId(Integer productid);

    int updateByPrimaryKeySelective(Version1 record);

    int updateByPrimaryKey(Version1 record);
}