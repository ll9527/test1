package com.serviceImp;

import com.dao.SellerBcImgMapper;
import com.dao.SellerMapper;
import com.entity.Product;
import com.entity.Seller;
import com.entity.SellerBcImg;
import com.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SellerServiceImple implements SellerService {

    @Autowired(required = false)
    private SellerMapper sellerMapper;
    @Autowired(required = false)
    private SellerBcImgMapper sellerBcImgMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return sellerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Seller record) {
        return sellerMapper.insert(record);
    }

    @Override
    public int insertSelective(Seller record) {
        return sellerMapper.insertSelective(record);
    }

    @Override
    public Seller selectByPrimaryKey(Integer id) {
        return sellerMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Seller record) {
        return sellerMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Seller record) {
        return sellerMapper.updateByPrimaryKey(record);
    }

    @Override
    public int selectKey(Seller record) {
        return sellerMapper.selectKey(record);
    }

    @Override
    public Seller selectByUserid(Integer userid) {
        return sellerMapper.selectByUserid(userid);
    }

    @Override
    public int deleteByUserid(Integer userid) {
        return sellerMapper.deleteByUserid(userid);
    }

    @Override
    public Seller selectUnconfirmDetial(Integer sellerid) {
        return sellerMapper.selectUnconfirmDetial(sellerid);
    }

    @Override
    public List<Seller> selectSellerTopThree(Integer sellerid) {
        return sellerMapper.selectSellerTopThree(sellerid);
    }

    @Override
    public List<Seller> selectSellerTopNine(Integer sellerid) {
        return sellerMapper.selectSellerTopNine(sellerid);
    }

    @Override
    public List<Seller> selectSellerFromSellerClass(String sellerClass) {
        return sellerMapper.selectSellerFromSellerClass(sellerClass);
    }

    @Override
    public List<Seller> selectAll() {
        return sellerMapper.selectAll();
    }

    @Override
    public List<Seller> selectSellerDetail(Map<String, Object> map) {
        return sellerMapper.selectSellerDetail(map);
    }
    
    @Override
    public List<SellerBcImg> selectBySId(Integer sellerId){
    	return sellerBcImgMapper.selectBySId(sellerId);
    }
}
