package com.service;

import com.entity.Product;
import com.entity.ProductCommentFreight;

import java.util.List;
import java.util.Map;

public interface ProductService {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    //查询所有（包括商品封面）
    List<Product> selectAll();

    //selectKey
    int selectKey(Product record);

    //查询商品详情
    Map selectProductDetail(Integer productid);

    //查询商品+封面
    Product selectProductWithImgF(Integer productid);

    //查询一级目录下的商品和商品封面
    List<Product> selectLevel1P(Integer classid);

    //查询二级目录下的商品和商品封面
    List<Product> selectLevel2P(Map<String, Object> map);

    //查询商户内商品
    List<Product> selectFromSeller(Integer sellerid);

    //全局模糊查询
    List<Product> serchProduct(Map<String, Object> map);

    Integer setPFreight(ProductCommentFreight pcf);
    /**
     * 用商品id查评论
     * @param pId
     * @return
     */
	List<ProductCommentFreight> selectCommentsByPid(Integer pId);
	/**
	 * 查看所有商品
	 * @return
	 */
	List selectAllProduct();
	/**
	 * 查看平台活动的商品
	 * @param sta
	 * @return
	 */
	List selectActivityPro(Integer sta);
	/**
	 * 下架平台的活动商品
	 * @param productid
	 * @return
	 */
	Integer productDown(Integer productid);
	/**
	 * 查看平台活动商品的详情信息
	 * @param productid
	 * @return
	 */
	Map selectAdminProDetail(Integer productid);

}
