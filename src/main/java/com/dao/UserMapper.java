package com.dao;

import com.entity.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(Map<String, Object> map);

    //根据userid关联查询address
    User selectAddressByUserId(Integer userid);

    //查询已提交商家入驻申请但未确认的用户
    List<User> selectUnconfirmed();

    //查询isSeller为1的User信息+商铺名字
    User selectWithSeller(Integer userid);

    //注册验证手机号码
    User checkTel(Long tel);

    //通过userid查询是否为提交申请用户
    User selectUnconfirmByUserId(Integer userid);

    //selectKey
    Integer selectKey(User record);
    /**
     * 查询用户人数
     * @return
     */
	Integer selectAllUser();
	/**
	 * 查询Vip人数
	 * @return
	 */
	Integer selectVipUser();
	/**
	 * 查询达到3800积分需求的Vip
	 * @return
	 */
	List<User> selectVipMeet();
	/**
	 * 查询所有积分
	 * @return
	 */
	Integer selectAllScore();
	/**
	 * 查积分不为0 的user
	 * @return
	 */
	List<User> selectUser();
}