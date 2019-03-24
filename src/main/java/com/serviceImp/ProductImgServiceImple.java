package com.serviceImp;

import com.dao.ProductImgMapper;
import com.entity.ProductImg;
import com.service.ProductImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImgServiceImple implements ProductImgService {

    @Autowired(required = false)
    private ProductImgMapper productImgMapper;


    @Override
    public int deleteByPrimaryKey(Integer id) {
        return productImgMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ProductImg record) {
        return insert(record);
    }

    @Override
    public int insertSelective(ProductImg record) {
        return productImgMapper.insertSelective(record);
    }

    @Override
    public ProductImg selectByPrimaryKey(Integer id) {
        return productImgMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ProductImg record) {
        return productImgMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ProductImg record) {
        return productImgMapper.updateByPrimaryKey(record);
    }
}
