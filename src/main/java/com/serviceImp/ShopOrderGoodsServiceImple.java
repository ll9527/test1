package com.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.ShopOrderGoodsMapper;
import com.entity.ShopOrderGoods;
import com.service.ShopOrderGoodsService;

@Service
@Transactional
public class ShopOrderGoodsServiceImple implements ShopOrderGoodsService {
	@Autowired
	private ShopOrderGoodsMapper shopOrderGoodsMapper;
	
	@Override
	public int insertSelective(ShopOrderGoods sog) {
		return shopOrderGoodsMapper.insertSelective(sog);
	}

}
