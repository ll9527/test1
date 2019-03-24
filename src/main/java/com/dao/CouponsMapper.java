package com.dao;

import java.util.List;

import com.entity.Coupons;

public interface CouponsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Coupons record);

    int insertSelective(Coupons record);

    Coupons selectByPrimaryKey(Integer id);

    List<Coupons> selectByUserId(Integer userId);
    
    int updateByPrimaryKeySelective(Coupons record);

    int updateByPrimaryKey(Coupons record);
}