package com.dao;

import com.entity.Collect;

import java.util.List;
import java.util.Map;

public interface CollectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Collect record);

    int insertSelective(Collect record);

    Collect selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Collect record);

    int updateByPrimaryKey(Collect record);

    int selectBySellerId(Integer sellerid);

    Collect selectByUseridOr(Map<String, Object> map);

    List<Collect> selectProductByUserid(Integer userid);

    List<Collect> selectByProductid(Integer productid);
}