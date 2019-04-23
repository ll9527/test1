package com.dao;

import java.util.List;
import java.util.Map;

import com.entity.Referrer;

public interface ReferrerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Referrer record);

    int insertSelective(Referrer record);

    Referrer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Referrer record);

    int updateByPrimaryKey(Referrer record);
    /**
     * 用uid查推荐人id
     * @param id
     * @return
     */
	Referrer selectByUId(Integer userId);
	/**
	 * 用推荐人id查所有已注册的用户
	 * @param referrerId
	 * @return
	 */
	List<Referrer> selectByRId(Integer referrerId);
}