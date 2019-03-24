package com.serviceImp;

import com.dao.CollectMapper;
import com.entity.Collect;
import com.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CollectServiceImple implements CollectService {

    @Autowired(required = false)
    private CollectMapper collectMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return collectMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Collect record) {
        return collectMapper.insert(record);
    }

    @Override
    public int insertSelective(Collect record) {
        return collectMapper.insertSelective(record);
    }

    @Override
    public Collect selectByPrimaryKey(Integer id) {
        return collectMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Collect record) {
        return collectMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Collect record) {
        return collectMapper.updateByPrimaryKey(record);
    }

    @Override
    public int selectBySellerId(Integer sellerid) {
        return collectMapper.selectBySellerId(sellerid);
    }

    @Override
    public Collect selectByUseridOr(Map<String, Object> map) {
        return collectMapper.selectByUseridOr(map);
    }

    @Override
    public List<Collect> selectProductByUserid(Integer userid) {
        return collectMapper.selectProductByUserid(userid);
    }

    @Override
    public List<Collect> selectByProductid(Integer productid) {
        return collectMapper.selectByProductid(productid);
    }
}
