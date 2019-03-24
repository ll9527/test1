package com.service;

import com.entity.Collect;

import java.util.List;
import java.util.Map;

public interface CollectService {
    int deleteByPrimaryKey(Integer id);

    int insert(Collect record);

    int insertSelective(Collect record);

    Collect selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Collect record);

    int updateByPrimaryKey(Collect record);

    //通过sellerid查询商家收藏条数
    int selectBySellerId(Integer sellerid);

    //通过userid+sellerid或productid查询单条数据
    Collect selectByUseridOr(Map<String,Object> map);

    //通过userid查询多条商品收藏数据
    List<Collect> selectProductByUserid(Integer userid);

    //通过productid查询
    List<Collect> selectByProductid(Integer productid);
}
