package com.dao;

import java.util.List;

import com.entity.ProductCommentFreight;

public interface ProductCommentFreightMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductCommentFreight record);

    int insertSelective(ProductCommentFreight record);

    ProductCommentFreight selectByPrimaryKey(Integer id);
    
    /**
     * 用商品id查询该商品得运费
     * @param productId
     * @return List<ProductCommentFreight>
     */
    List<ProductCommentFreight> selectByProductId(Integer productId);

    int updateByPrimaryKeySelective(ProductCommentFreight record);

    int updateByPrimaryKey(ProductCommentFreight record);
}