package com.serviceImp;

import com.dao.ReferrerMapper;
import com.entity.Referrer;
import com.service.ReferrerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferrerServiceImple implements ReferrerService {

    @Autowired(required = false)
    private ReferrerMapper referrerMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return referrerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Referrer record) {
        return referrerMapper.insert(record);
    }

    @Override
    public int insertSelective(Referrer record) {
        return referrerMapper.insertSelective(record);
    }

    @Override
    public Referrer selectByPrimaryKey(Integer id) {
        return referrerMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Referrer record) {
        return referrerMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Referrer record) {
        return referrerMapper.updateByPrimaryKey(record);
    }
}
