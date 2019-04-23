package com.service;

import com.entity.Product;
import com.entity.Seller;
import com.entity.SellerAddress;
import com.entity.SellerBcImg;
import com.entity.SellerCover;

import java.util.List;
import java.util.Map;

public interface SellerService {
    int deleteByPrimaryKey(Integer id);

    int insert(Seller record);

    int insertSelective(Seller record);

    Seller selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Seller record);

    int updateByPrimaryKey(Seller record);

    //selectKey
    int selectKey(Seller record);

    //selectByUserid
    Seller selectByUserid(Integer userid);

    //deleteByUserid
    int deleteByUserid(Integer userid);

    //查询未审批详情
    Seller selectUnconfirmDetial(Integer sellerid);

    //根据selerid查找商家旗下前三热销商品
    List<Seller> selectSellerTopThree(Integer sellerid);

    //查找商家热销商品前九
    List<Seller> selectSellerTopNine(Integer sellerid);

    //通过sellerClass查找seller
    List<Seller> selectSellerFromSellerClass(String sellerClass);

    //查询所有
    List<Seller> selectAll();

    //查询商家所有上架商品+图片
    List<Seller> selectSellerDetail(Map<String, Object> map);
    /**
     * 用商家id查询商家的证件图片
     * @param sellerId
     * @return
     */
    public List<SellerBcImg> selectBySId(Integer sellerId);
    /**
     * 用商家id查商家所有信息
     * @param sellerId
     * @return
     */
	Map getSeller(Integer sellerId);
	/**
	 * 查在地区的所有商家
	 * @return
	 */
	List<SellerAddress> selectSellerLikeCity(String currentCity);
	/**
	 * 查询地区的指定类型商家
	 * @param sellerClass
	 * @param currentCity
	 * @return
	 */
	List<Seller> selectSellerFromSellerClass1(String sellerClass, String currentCity);
	/**
	 * 插入商家图片记录数据
	 * @param sellerCover
	 */
	void addSellerCover(SellerCover sellerCover);
	/**
	 * 用商家id查商家图片
	 * @param id
	 * @return
	 */
	List<SellerCover> selectSCover(Integer id);
}
