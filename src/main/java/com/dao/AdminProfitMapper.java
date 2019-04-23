package com.dao;

import java.math.BigDecimal;
import java.util.List;

import com.entity.AdminImg;
import com.entity.AdminProfit;

public interface AdminProfitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminProfit record);

    int insertSelective(AdminProfit record);

    AdminProfit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdminProfit record);

    int updateByPrimaryKey(AdminProfit record);

    AdminProfit selectAdminByVipMoney();

	String selectGG();

}