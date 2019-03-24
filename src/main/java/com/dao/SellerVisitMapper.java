package com.dao;

import com.entity.SellerVisit;

import java.util.List;

public interface SellerVisitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SellerVisit record);

    int insertSelective(SellerVisit record);

    SellerVisit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SellerVisit record);

    int updateByPrimaryKey(SellerVisit record);

    SellerVisit selectBySellerid(Integer sellerid);

    List<SellerVisit> selectAll();
}