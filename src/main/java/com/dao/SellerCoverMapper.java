package com.dao;

import java.util.List;

import com.entity.SellerCover;

public interface SellerCoverMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SellerCover record);

    int insertSelective(SellerCover record);

    SellerCover selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SellerCover record);

    int updateByPrimaryKey(SellerCover record);
    /**
     * 用商家id查商家图片
     * @param id
     * @return
     */
	List<SellerCover> selectSCover(Integer id);
}