package com.service;

import com.entity.Address;

import java.util.List;

public interface AddressService {
    int deleteByPrimaryKey(Integer id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

    List<Address> selectByUserId(Integer userid);
}
