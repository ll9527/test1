package com.dao;

import java.util.List;

import com.entity.AdminProduct;

public interface AdminProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminProduct record);

    int insertSelective(AdminProduct record);

    AdminProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdminProduct record);

    int updateByPrimaryKey(AdminProduct record);
    /**
     * 查看平台活动的商品
     * @param sta
     * @return
     */
	List<AdminProduct> selectProBySta(Integer sta);
	/**
     * 用商品id查看平台活动的商品
     * @param sta
     * @return
     */
	AdminProduct selectByPid(Integer productid);
}