package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.entity.ShopProductSpecification;

public interface ShopProductSpecificationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopProductSpecification record);

    int insertSelective(ShopProductSpecification record);

    ShopProductSpecification selectByPrimaryKey(Integer id);
    /**
     * 查1级规格
     * @param productId
     * @return
     */
    List<ShopProductSpecification> selectLevel1Speci(Integer productId);
    /**
     * 查2级规格
     * @param productId
     * @return
     */
    List<ShopProductSpecification> selectLevel2Speci(@Param("productId")Integer productId);
    /**
     * 查所有sku
     * @param productId
     * @return
     */
    List<ShopProductSpecification> selectSku(Integer productId);
    
    int updateByPrimaryKeySelective(ShopProductSpecification record);

    int updateByPrimaryKey(ShopProductSpecification record);
}