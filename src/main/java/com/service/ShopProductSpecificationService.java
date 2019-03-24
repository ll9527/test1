package com.service;

import com.entity.ShopProductSpecification;

public interface ShopProductSpecificationService {

	/**
	 * 插入型号数据，可以获得id
	 * @param version
	 * @return
	 */
	int insertSelective(ShopProductSpecification version);
	
}
