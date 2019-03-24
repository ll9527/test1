package com.serviceImp;

import com.dao.VersionMapper;
import com.entity.Version;
import com.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VersionServiceImple implements VersionService {

    @Autowired(required = false)
    private VersionMapper versionMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return versionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Version record) {
        return versionMapper.insert(record);
    }

    @Override
    public int insertSelective(Version record) {
        return versionMapper.insertSelective(record);
    }

    @Override
    public Version selectByPrimaryKey(Integer id) {
        return versionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Version record) {
        return versionMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Version record) {
        return versionMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Version> selectVersionByPid(Integer productid) {
        return versionMapper.selectVersionByPid(productid);
    }
}
