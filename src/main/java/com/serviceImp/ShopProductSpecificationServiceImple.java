package com.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.ShopProductSpecificationMapper;
import com.entity.ShopProductSpecification;
import com.service.ShopProductSpecificationService;

@Service
public class ShopProductSpecificationServiceImple implements ShopProductSpecificationService {

	@Autowired
	private ShopProductSpecificationMapper shopProductSpecificationMapper;
	
	@Override
	public int insertSelective(ShopProductSpecification version) {
		return shopProductSpecificationMapper.insertSelective(version);
	}

}
