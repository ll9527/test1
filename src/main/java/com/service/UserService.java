package com.service;

import com.entity.Address;
import com.entity.Coupons;
import com.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(Map<String, Object> map);

    //查询已提交商家入驻申请但未确认的用户
    List<User> selectUnconfirmed();

    //根据userid关联查询address
    User selectAddressByUserId(Integer userid);

    /**
     * 用userid查最新的创建的地址和用户
     * @param userid
     * @return
     */
    Map<String, Object> selectNewAddressByUserId(Integer userid);

    //查询isSeller为1的User信息+商铺名字
    User selectWithSeller(Integer userid);

    //注册验证手机号码
    User checkTel(Long tel);

    //通过userid查询是否为提交申请用户
    Integer selectUnconfirmByUserId(Integer userid);

    //selectKey
    Integer selectKey(User record);

    /**
     * 查询优惠券和商品运费
     * @param userid
     * @param productid
     * @return
     */
	Map<String, Object> selectFreightAndCoupons(Integer userid, Integer productid);
	/**
	 * 更新优惠券状态
	 * @param coupons
	 */
	void updateCoupons(Coupons coupons);
	/**
	 * 查询公告
	 * @return
	 */
	String selectGG();
	/**
	 * 查用户人数
	 * @return
	 */
	Integer selectAllUser();
	/**
	 * 查Vip人数
	 * @return
	 */
	Integer selectVipUser();
	/**
	 * 发钱给达到要求vip
	 */
	void sendVipMoney();
	/**
	 * 清空30天内没购物的会员积分
	 */
	void removeScore();
	/**
	 * 用户分享
	 * @param uwea
	 */
	void share(Integer uwea);

	Map<String, Object> jiangjin();

	int shareNum(Integer uuu);
}
