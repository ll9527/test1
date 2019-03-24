package com.dao;

import com.entity.SellerBcImg;

import java.util.List;

public interface SellerBcImgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SellerBcImg record);

    int insertSelective(SellerBcImg record);

    SellerBcImg selectByPrimaryKey(Integer id);
    /**
     * 用商家id查询商家证件图片
     * @param sellerId
     * @return
     */
    List<SellerBcImg> selectBySId(Integer sellerId);

    int updateByPrimaryKeySelective(SellerBcImg record);

    int updateByPrimaryKey(SellerBcImg record);

    List<SellerBcImg> selectByUserid(Integer userid);

    int deleteByUserid(Integer userid);
}