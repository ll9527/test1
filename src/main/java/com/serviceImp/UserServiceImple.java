package com.serviceImp;

import com.dao.AddressMapper;
import com.dao.AdminProfitMapper;
import com.dao.CouponsMapper;
import com.dao.Md5Util;
import com.dao.ProductCommentFreightMapper;
import com.dao.ProductMapper;
import com.dao.ReferrerMapper;
import com.dao.ShopOrderMapper;
import com.dao.UserMapper;
import com.entity.Address;
import com.entity.AdminProfit;
import com.entity.Coupons;
import com.entity.ProductCommentFreight;
import com.entity.Referrer;
import com.entity.ShopOrder;
import com.entity.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImple implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private ProductCommentFreightMapper pfm;
    @Autowired(required = false)
    private CouponsMapper couponsMapper;
    @Autowired(required = false)
    private AddressMapper addressMapper;
    @Autowired(required = false)
    private AdminProfitMapper adminProfitMapper;
    @Autowired(required = false)
    private ShopOrderMapper shopOrderMapper;
    @Autowired(required = false)
    private ReferrerMapper referrerMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(User record) {
        return userMapper.insert(record);
    }

    @Override
    public int insertSelective(User record) {
        Md5Util md5Util = new Md5Util();
        record.setPassword(md5Util.md5(record.getPassword()));
        return userMapper.insertSelective(record);
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }

    @Override
    public User login(Map<String, Object> map) {
        Md5Util md5Util = new Md5Util();
        map.put("password", md5Util.md5(map.get("password").toString()));
        return userMapper.login(map);
    }

    @Override
    public List<User> selectUnconfirmed() {
        return userMapper.selectUnconfirmed();
    }

    @Override
    public User selectAddressByUserId(Integer userid) {
        return userMapper.selectAddressByUserId(userid);
    }

    @Override
    public User selectWithSeller(Integer userid) {
        return userMapper.selectWithSeller(userid);
    }

    @Override
    public User checkTel(Long tel) {
        return userMapper.checkTel(tel);
    }

    @Override
    public Integer selectUnconfirmByUserId(Integer userid) {
        User user = userMapper.selectUnconfirmByUserId(userid);
        if(user==null){
            return -1;
        }else{
            return user.getIsApply();
        }
    }

    @Override
    public Integer selectKey(User record) {
        return userMapper.selectKey(record);
    }

	@Override
	public Map<String, Object> selectFreightAndCoupons(Integer userid, Integer productid) {
		Map<String, Object> map = new HashMap<String, Object>();
		//查询优惠券
		List<Coupons> couponsList = couponsMapper.selectByUserId(userid);
		if(!couponsList.isEmpty()) {
			map.put("couponsMoney", couponsList.get(0).getPreferentialMoney());
			map.put("coupons", couponsList.get(0));
		}else {
			map.put("couponsMoney", 0);
		}
		//查询商品运费
		List<ProductCommentFreight> pfList = pfm.selectByProductId(productid);
		if(!pfList.isEmpty()) {
			map.put("freight", pfList.get(0).getFreight());
		}else {
			map.put("freight", 0);
		}
		return map;
	}

	@Override
	public Map<String, Object> selectNewAddressByUserId(Integer userid) {
		List<Address> addressList = addressMapper.selectNewAddressByUserId(userid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("addressList", addressList);
		return map;
	}

	@Override
	public void updateCoupons(Coupons coupons) {
		couponsMapper.updateByPrimaryKeySelective(coupons);
	}

	@Override
	public String selectGG() {
		return adminProfitMapper.selectGG();
	}

	@Override
	public Integer selectAllUser() {
		return userMapper.selectAllUser();
	}

	@Override
	public Integer selectVipUser() {
		return userMapper.selectVipUser();
	}

	@Override
	public void sendVipMoney() {
		List<User> list = userMapper.selectVipMeet();
		Integer score = userMapper.selectAllScore();
		AdminProfit ap = adminProfitMapper.selectAdminByVipMoney();
		if(ap.getBonusPools().compareTo(new BigDecimal(0)) == 1) {
//			BigDecimal price = ap.getBonusPools().divide(new BigDecimal(score.toString()),9,BigDecimal.ROUND_HALF_EVEN);
			for (User user : list) {
//				user.setMoney(user.getMoney().add(new BigDecimal(user.getScore().toString()).multiply(price)).setScale(2,BigDecimal.ROUND_DOWN));
				user.setMoney(user.getMoney().add(
						ap.getBonusPools().multiply(
								new BigDecimal(user.getScore().toString()).divide(
										new BigDecimal(score.toString()),2,BigDecimal.ROUND_HALF_EVEN
									)
								)
							)
						);
				userMapper.updateByPrimaryKeySelective(user);
			}
		}
	}

	@Override
	public void removeScore() {
		List<User> list = userMapper.selectUser();
		Long time = 2592000000L;
		if(!list.isEmpty()) {
			for (User user : list) {
				List<ShopOrder> orderList = shopOrderMapper.selectUserLastOrder(user.getId());
				if(!orderList.isEmpty()) {
					ShopOrder order = orderList.get(0);
					Date date = new Date();
			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        Long diff = null;
			        if (order != null) {
			            Date createTime = order.getEndTime();
			            try {
			                diff = sdf.parse(sdf.format(date)).getTime() - sdf.parse(sdf.format(createTime)).getTime();
//			                如果时间查大于30天就清除积分
			                if(diff >= time) {
			                	user.setScore(0);
			                	userMapper.updateByPrimaryKeySelective(user);
			                }
			            } catch (ParseException e) {
			                e.printStackTrace();
			            }
			        }
				}
			}
		}
	}

	@Override
	public void share(Integer uwea) {
		AdminProfit ap = adminProfitMapper.selectAdminByVipMoney();
		if(ap.getDiscountAmount().compareTo(new BigDecimal(0)) == 1) {
			User user = userMapper.selectByPrimaryKey(uwea);
			Random random = new Random();
			BigDecimal money = new BigDecimal(random.nextInt(11));
			if(ap.getDiscountAmount().compareTo(money) == 1) {
				ap.setDiscountAmount(ap.getDiscountAmount().subtract(money));
				user.setMoney(user.getMoney().add(money));
			}else {
				user.setMoney(user.getMoney().add(ap.getDiscountAmount()));
				ap.setDiscountAmount(new BigDecimal(0));
			}
			adminProfitMapper.updateByPrimaryKeySelective(ap);
			userMapper.updateByPrimaryKeySelective(user);
		}
		
	}

	@Override
	public Map<String, Object> jiangjin() {
		Map<String, Object> map = new HashMap();
		AdminProfit ap = adminProfitMapper.selectAdminByVipMoney();
		map.put("discountAmount", ap.getDiscountAmount());
		map.put("bonusPools", ap.getBonusPools());
		return map;
	}

	@Override
	public int shareNum(Integer uuu) {
		return referrerMapper.selectByRId(uuu).size();
	}

}
