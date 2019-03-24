package com.service;

import com.entity.SellerBcImg;

import java.util.List;

public interface SellerBcImgService {
    int deleteByPrimaryKey(Integer id);

    int insert(SellerBcImg record);

    int insertSelective(SellerBcImg record);

    SellerBcImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SellerBcImg record);

    int updateByPrimaryKey(SellerBcImg record);

    int deleteByUserid(Integer userid);

    List<SellerBcImg> selectByUserid(Integer userid);
}
