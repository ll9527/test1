package com.dao;

import java.util.List;

import com.entity.AwardHistory;

public interface AwardHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AwardHistory record);

    int insertSelective(AwardHistory record);

    AwardHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AwardHistory record);

    int updateByPrimaryKey(AwardHistory record);
    /**
     * 用admin_product表的id和用户id查是否领取过
     * @param id
     * @return
     */
	AwardHistory selectByAProId(Integer id, Integer userid);
	/**
	 * 用用户id查询已领取平台奖品的记录
	 * @param uuu
	 * @return
	 */
	List<AwardHistory> selectByUId(Integer uuu);
}