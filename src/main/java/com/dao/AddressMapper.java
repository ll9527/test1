package com.dao;

import com.entity.Address;

import java.util.List;

public interface AddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

    List<Address> selectByUserId(Integer userid);

    /**
     * 通过userid查询地址信息  最新的排前面
     * @param userid
     * @return
     */
	List<Address> selectNewAddressByUserId(Integer userid);
}