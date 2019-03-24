package com.serviceImp;

import com.dao.ShopMapper;
import com.entity.Shop;
import com.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImple implements ShopService {

    @Autowired(required = false)
    private ShopMapper shopClassifyMapper;

    @Override
    public int deleteByPrimaryKey(Integer classId) {
        return shopClassifyMapper.deleteByPrimaryKey(classId);
    }

    @Override
    public int insert(Shop record) {
        return shopClassifyMapper.insert(record);
    }

    @Override
    public int insertSelective(Shop record) {
        return shopClassifyMapper.insertSelective(record);
    }

    @Override
    public Shop selectByPrimaryKey(Integer classId) {
        return shopClassifyMapper.selectByPrimaryKey(classId);
    }

    @Override
    public int updateByPrimaryKeySelective(Shop record) {
        return shopClassifyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Shop record) {
        return shopClassifyMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Shop> selectAll() {
        return shopClassifyMapper.selectAll();
    }

    @Override
    public List<Shop> selectLevel() {
        return shopClassifyMapper.selectLevel();
    }

    @Override
    public List<Shop> selectTwo() {
        return shopClassifyMapper.selectTwo();
    }

    @Override
    public List<Shop> selectLevel1and2(Integer classid) {
        return shopClassifyMapper.selectLevel1and2(classid);
    }

}
