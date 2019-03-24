package com.dao;

import com.entity.ClassWithProduct;

import java.util.List;

public interface ClassWithProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassWithProduct record);

    int insertSelective(ClassWithProduct record);

    ClassWithProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClassWithProduct record);

    int updateByPrimaryKey(ClassWithProduct record);

    List<ClassWithProduct> selectByClassId(Integer classid);


}