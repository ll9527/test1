package com.serviceImp;

import com.dao.ClassWithProductMapper;
import com.entity.ClassWithProduct;
import com.service.ClassWithProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassWithProductServiceImple implements ClassWithProductService{

    @Autowired(required = false)
    private ClassWithProductMapper classWithProductMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return classWithProductMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ClassWithProduct record) {
        return classWithProductMapper.insert(record);
    }

    @Override
    public int insertSelective(ClassWithProduct record) {
        return classWithProductMapper.insertSelective(record);
    }

    @Override
    public ClassWithProduct selectByPrimaryKey(Integer id) {
        return classWithProductMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ClassWithProduct record) {
        return classWithProductMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ClassWithProduct record) {
        return classWithProductMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<ClassWithProduct> selectByClassId(Integer classid) {
        return classWithProductMapper.selectByClassId(classid);
    }


}
