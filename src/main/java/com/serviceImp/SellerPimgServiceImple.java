package com.serviceImp;

import com.dao.SellerWithProductImgMapper;
import com.entity.SellerWithProductImg;
import com.service.SellerPimgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerPimgServiceImple implements SellerPimgService {

    @Autowired(required = false)
    private SellerWithProductImgMapper sellerWithProductImgMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return sellerWithProductImgMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SellerWithProductImg record) {
        return sellerWithProductImgMapper.insert(record);
    }

    @Override
    public int insertSelective(SellerWithProductImg record) {
        return sellerWithProductImgMapper.insertSelective(record);
    }

    @Override
    public SellerWithProductImg selectByPrimaryKey(Integer id) {
        return sellerWithProductImgMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SellerWithProductImg record) {
        return sellerWithProductImgMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SellerWithProductImg record) {
        return sellerWithProductImgMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SellerWithProductImg> selectByPid(Integer productid) {
        return sellerWithProductImgMapper.selectByPid(productid);
    }
}
