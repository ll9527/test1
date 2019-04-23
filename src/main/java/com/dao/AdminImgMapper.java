package com.dao;

import java.util.List;

import com.entity.AdminImg;

public interface AdminImgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminImg record);

    int insertSelective(AdminImg record);

    AdminImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdminImg record);

    int updateByPrimaryKey(AdminImg record);
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
	 * 查看平台分享封面图
	 * @return
	 */
	List<AdminImg> selectAdminShareImg();
}