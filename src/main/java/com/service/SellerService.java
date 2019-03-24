package com.service;

import com.entity.Product;
import com.entity.Seller;
import com.entity.SellerBcImg;

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
}
