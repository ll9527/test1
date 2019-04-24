package com.serviceImp;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.AdminImgMapper;
import com.dao.AdminProfitMapper;
import com.entity.AdminImg;
import com.entity.AdminProfit;
import com.service.AdminProfitService;

@Service
@Transactional
public class AdminProfitServiceImpl implements AdminProfitService {
	
	@Autowired
	private AdminProfitMapper adminProfitMapper;
	@Autowired
	private AdminImgMapper adminImgMapper;

	@Override
	public AdminProfit selectAdmin() {
		return adminProfitMapper.selectAdminByVipMoney();
	}
	
	@Override
	public Integer updateByPrimaryKeySelective(AdminProfit ap) {
		return adminProfitMapper.updateByPrimaryKeySelective(ap);
	}

	@Override
	public Integer upPoint(Integer sta, String point) {
		AdminProfit ap = new AdminProfit();
		ap.setId(1);
		if(sta == 1) {
			ap.setDiscountPersent(new Integer(point));
		}else if(sta == 2) {
			ap.setPoolsPersent(new Integer(point));
		}else if(sta == 3) {
			ap.setShopPercent(new Double(point));
		}else {
			return 0;
		}
		return adminProfitMapper.updateByPrimaryKeySelective(ap);
	}

	@Override
	public List<AdminImg> selectAdminZImg() {
		return adminImgMapper.selectAdminZImg();
	}

	@Override
	public List<AdminImg> selectAdminGGImg() {
		return adminImgMapper.selectAdminGGImg();
	}

	@Override
	public void updataAdminImg(AdminImg adminImg) {
		adminImgMapper.updateByPrimaryKeySelective(adminImg);		
	}

	@Override
	public List<AdminImg> selectAdminShareImg() {
		return adminImgMapper.selectAdminShareImg();
	}

	@Override
	public void inserAdminImg(AdminImg adminImg) {
		adminImgMapper.insertSelective(adminImg);
	}

	@Override
	public int deleteZImg(int imgId) {
		// TODO Auto-generated method stub
		AdminImg img = adminImgMapper.selectByPrimaryKey(imgId);
		img.setRole(String.valueOf(-1));
		return adminImgMapper.updateByPrimaryKeySelective(img);
	}

}
