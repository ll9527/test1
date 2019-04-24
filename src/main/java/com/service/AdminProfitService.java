package com.service;

import java.math.BigDecimal;
import java.util.List;

import com.entity.AdminImg;
import com.entity.AdminProfit;

public interface AdminProfitService {

	AdminProfit selectAdmin();

	Integer updateByPrimaryKeySelective(AdminProfit ap);
	/**
	 * 设置抽点
	 * @param sta
	 * @param point
	 * @return
	 */
	Integer upPoint(Integer sta, String point);
	/**
	 * 查看平台证件照
	 * @return
	 */
	List<AdminImg> selectAdminZImg();
	/**
	 * 查看平台广告照
	 * @return
	 */
	List<AdminImg> selectAdminGGImg();
	/**
	 * 更新adminImg表
	 * @param adminImg
	 */
	void updataAdminImg(AdminImg adminImg);
	/**
	 * 查看平台分享封面
	 * @return
	 */
	List<AdminImg> selectAdminShareImg();
	/**
	 * 插入adminImg表数据
	 * @param adminImg
	 */
	void inserAdminImg(AdminImg adminImg);

	int deleteZImg(int imgId);
	
}
