package com.serviceImp;

import com.dao.SellerAddressMapper;
import com.entity.SellerAddress;
import com.service.SellerAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerAddressServiceImple implements SellerAddressService {

    @Autowired(required = false)
    private SellerAddressMapper sellerAddressMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return sellerAddressMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SellerAddress record) {
        return sellerAddressMapper.insert(record);
    }

    @Override
    public int insertSelective(SellerAddress record) {
        return sellerAddressMapper.insertSelective(record);
    }

    @Override
    public SellerAddress selectByPrimaryKey(Integer id) {
        return sellerAddressMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SellerAddress record) {
        return sellerAddressMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SellerAddress record) {
        return sellerAddressMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SellerAddress> selectByUserId(Integer userid) {
        return sellerAddressMapper.selectByUserId(userid);
    }

    @Override
    public int deleteBySellerId(Integer sellerid) {
        return sellerAddressMapper.deleteBySellerId(sellerid);
    }

    @Override
    public List<SellerAddress> selectBySellerid(Integer sellerid) {
        return sellerAddressMapper.selectBySellerid(sellerid);
    }
}
