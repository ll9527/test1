package com.serviceImp;

import com.dao.AddressMapper;
import com.entity.Address;
import com.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImple implements AddressService {

    @Autowired(required = false)
    private AddressMapper addressMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return addressMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Address record) {
        return addressMapper.insert(record);
    }

    @Override
    public int insertSelective(Address record) {
        return addressMapper.insertSelective(record);
    }

    @Override
    public Address selectByPrimaryKey(Integer id) {
        return addressMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Address record) {
        return addressMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Address record) {
        return addressMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Address> selectByUserId(Integer sellerid) {
        return addressMapper.selectByUserId(sellerid);
    }
}
