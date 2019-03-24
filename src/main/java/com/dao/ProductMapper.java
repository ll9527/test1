package com.dao;

import com.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectAll();

    int selectKey(Product record);

    //查询商品+封面
    Product selectProductWithImgF(Integer productid);

    //查询商品+详情图
    List<Product> selectProductWithImg(Integer productid);

    List<Product> selectLevel1P(Integer classid);

    List<Product> selectLevel2P(Map<String, Object> map);

    //查询商户内商品
    List<Product> selectFromSeller(Integer sellerid);

    //全局模糊查询
    List<Product> serchProduct(Map<String, Object> map);


}