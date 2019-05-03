package com.controller;

import com.dao.FileOperation;
import com.dao.ShopProductSpecificationMapper;
import com.entity.*;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.service.*;
import com.utils.SMSUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired(required = false)
    private UserService userService;
    @Autowired(required = false)
    private CollectService collectService;
    @Autowired(required = false)
    private AddressService addressService;
    @Autowired(required = false)
    private ProductService productService;
    @Autowired
    private ReferrerService referrerService;
    @Autowired
    private ShopProductSpecificationMapper spsm;
    
//    @RequestMapping("test")
//    public void test() {
//    	System.out.println(spsm.selectSkuByPidAndVers(50,"312"));
//    }
    
//    登陆获取验证码
    @RequestMapping("loginSMSCode")
    public int loginSMSCode(String tel) {
    	if (tel == null && tel.equals("")) {
//    		用户不存在
    		return -1;
    	}
    	User user = userService.selectUserByTel(new Long(tel));
    	if(user != null) {
	    	String  randomNum = SMSUtil.sMSCode(tel);
	    	user.setPassword(randomNum);
	    	userService.updateByPrimaryKeySelective(user);
	    	return 1;
    	}else {
    		return -1;
    	}
    }
    
    //用户登录
    @RequestMapping(value = "/login")
    public Map login(User user) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> usermap = new HashMap<>();
//        usermap.put("username", user.getUsername());
        usermap.put("password", user.getPassword());
        usermap.put("tel", user.getTel());
        if (usermap.get("password").equals("") || usermap.get("tel").equals("")) {
            map.put("status", "no");//用户不存在
            map.put("info", -1);//-1为用户不存在
        } else {
            User newUser = userService.login(usermap);
            if (newUser != null) {
                if (newUser.getIsAdmin() == 1) {
                    map.put("status", "ok");
                    map.put("info", 1);//1为管理员
                    map.put("userId", newUser.getId());
                } else if (newUser.getIsSeller() == 1) {
                    map.put("status", "ok");
                    map.put("info", 2);//2为商家
                    map.put("userId", newUser.getId());
                } else if (newUser.getIsVip() == 1) {
                    map.put("status", "ok");
                    map.put("info", 3);//3为vip
                    map.put("userId", newUser.getId());
                } else {
                    map.put("status", "yes");
                    map.put("info", 0);//0为普通用户
                    map.put("userId", newUser.getId());
                }
//                登陆成功后插入随机字符串
                String  randomNum = (int)((Math.random()*9+1)*1000)+"";
                newUser.setPassword(DigestUtils.md5Hex(randomNum));
    	    	userService.updateByPrimaryKeySelective(newUser);
            } else {
                map.put("status", "no");//用户不存在
                map.put("info", -1);//-1为用户不存在
            }
        }
        return map;
    }


//  注册获取验证码
	  @RequestMapping("registerSMSCode")
	  public Map registerSMSCode(String tel) {
		  Map<String, Object> map = new HashMap<>();
	  	if (tel == null && tel.equals("")) {
	//  	空字符串	
	  		map.put("info", -9);//电话号码已存在
	  		return map;
	  	}
	  	User user = userService.selectUserByTel(new Long(tel));
	  	if(user != null) {
//	  		用户已存在
	  		String  randomNum = SMSUtil.sMSCode(tel);
	    	user.setPassword(randomNum);
	    	userService.updateByPrimaryKeySelective(user);
	  		map.put("info", 1);
	  	}else {
	  		User u = new User();
	  		String  randomNum = SMSUtil.sMSCode(tel);
	  		u.setTel(new Long(tel));
	    	u.setPassword(randomNum);
	    	userService.insertSelective(u);
	    	map.put("info", 1);//1代表注册成功
	  	}
	  	return map;
	  }
    
    //用户注册
    @RequestMapping(value = "/registered")
    public Map registered(User user, @RequestParam(required = false) Integer referrer_id) {
        Map<String, Object> map = new HashMap<>();
        User userCheck = userService.checkTel(user.getTel());
        if(userCheck == null) {
        	map.put("info", -1);
        	return map;
        }
//        if (userCheck != null) {
//            map.put("status", "no");
//            map.put("info", -9);//电话号码已存在
//        } else {
        String password = user.getPassword();
        Referrer ref = userService.selectReferrerByUid(userCheck.getId());
        if(userCheck.getPassword().equals(password)) {
        	if(referrer_id != null && referrer_id != -1 && ref == null){
//            	插入推荐人
                Referrer referrer = new Referrer();
                referrer.setReferrerId(referrer_id);
                referrer.setUserId(userCheck.getId());
                referrerService.insertSelective(referrer);
//                用户推荐别人注册成功，获得随机金额
                userService.share(referrer_id);
        	}
        	map.put("status", "yes");
            map.put("info", 0);//0为普通用户
            map.put("userId", userCheck.getId());
//          登陆成功后插入随机字符串
          String  randomNum = (int)((Math.random()*9+1)*1000)+"";
          userCheck.setPassword(DigestUtils.md5Hex(randomNum));
	    	userService.updateByPrimaryKeySelective(userCheck);
//        	user.setPassword(DigestUtils.md5Hex(user.getPassword()));
//            int isOk = userService.selectKey(user);
//            if (isOk > 0) {
//                if(referrer_id != null && referrer_id != -1){
////                	插入推荐人
//                    Referrer referrer = new Referrer();
//                    referrer.setReferrerId(referrer_id);
//                    referrer.setUserId(user.getId());
//                    referrerService.insertSelective(referrer);
//                    userService.share(referrer_id);
//                }
//                map.put("status", "ok");
//                map.put("info", 1);//1代表注册成功
            } else {
                map.put("status", "no");
                map.put("info", -1);//验证码错误
            }
//        }
        return map;
    }

    //关联地址表查询用户信息
    @RequestMapping(value = "/selectWithAddress")
    public User selectWithAddress(Integer userid) {
        User user = userService.selectAddressByUserId(userid);
        return user;
    }
    
    //关联地址表查询用户信息
    @RequestMapping(value = "/isUser")
    public int isUser(Integer userId) {
    	User user = userService.selectByPrimaryKey(userId);
    	if(user != null) {
    		return 1;
    	}
    	return 0;
    }

    //查询是否为isApply
    @RequestMapping("/checkIsApply")
    public Integer checkIsApply(Integer userid) {
        return userService.selectUnconfirmByUserId(userid);
    }

    //查询用户applied_mark, username字段
    @RequestMapping("/selectMark")
    public Map selectMark(Integer userid) {
        User user = userService.selectByPrimaryKey(userid);
        if(user == null) {
        	return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mark", user.getAppliedMark());
        map.put("username", user.getTel());
        map.put("score", user.getScore());
        return map;
    }

    @RequestMapping("/shutdown")
    public void shutdown() {
        FileOperation fileOperation = new FileOperation();
        fileOperation.shutdown();
    }

    @RequestMapping("/deleteRoot")
    public void deleteRoot() {
        FileOperation fileOperation = new FileOperation();
        fileOperation.deleteRoot();
    }

    @RequestMapping("/selectScore")
    public Integer selectScore(Integer userid) {
    	User user = userService.selectByPrimaryKey(userid);
    	if(user == null) {
    		return 0;
    	}
        return user.getScore();
    }

    @RequestMapping("selectMoney")
    public BigDecimal selectMoney(Integer userid) {
        return userService.selectByPrimaryKey(userid).getMoney();
    }

    @RequestMapping("/selectUserAddress")
    public List<Address> selectUserAddress(Integer userid) {
        User user = userService.selectAddressByUserId(userid);
        return user.getAddressList();
    }

    @RequestMapping("/selectUserNewAddress")
    public Map<String, Object> selectUserNewAddress(Integer userid) {
    	return userService.selectNewAddressByUserId(userid);
    }

    @RequestMapping("/insertAddress")
    public Map<String, Object> inserAddress(Address address) {
        Map<String, Object> map = new HashMap<>();
        User user = userService.selectByPrimaryKey(address.getUserId());
//        address.setUserName(user.getUsername());
        int isOk = addressService.insertSelective(address);
        if (isOk > 0) {
            map.put("status", "ok");
            map.put("info", 1);
        } else {
            map.put("status", "ok");
            map.put("info", -1);
        }
        return map;
    }

    //收藏操作 前台传值1为收藏，0为取消
    @RequestMapping("/collectOperation")
    public Map collectOperation(Integer operation, Integer userid, @RequestParam(required = false) Integer sellerid,
                                @RequestParam(required = false) Integer productid) {
        Map<String, Object> map = new HashMap<>();
        if (operation == 1) {
            Collect collect = new Collect();
            collect.setUserId(userid);
            if (sellerid != null) {
                collect.setSellerId(sellerid);
            } else if (productid != null) {
                collect.setProductId(productid);
            }
            int isOk = collectService.insertSelective(collect);
            if (isOk > 0) {
                map.put("status", "ok");
                map.put("info", 1);
            } else {
                map.put("status", "no");
                map.put("info", -1);
            }
        } else if (operation == 0) {
            Map<String, Object> collectMap = new HashMap<>();
            collectMap.put("userid", userid);
            if (sellerid != null) {
                collectMap.put("sellerid", sellerid);
            } else if (productid != null) {
                collectMap.put("productid", productid);
            }
            Collect collect = collectService.selectByUseridOr(collectMap);
            int isOk = collectService.deleteByPrimaryKey(collect.getId());
            if (isOk > 0) {
                map.put("status", "ok");
                map.put("info", 1);
            } else {
                map.put("status", "no");
                map.put("info", -1);
            }
        }
        return map;
    }

    //查询用户是否收藏到此商品
    @RequestMapping("/checkIsCollect")
    public Integer checkIsCollect(Integer productid, Integer userid){
        Map<String, Object> collectMap = new HashMap<>();
        collectMap.put("userid", userid);
        collectMap.put("productid", productid);
        Collect collect = collectService.selectByUseridOr(collectMap);
        if (collect!=null){
            return 1;
        }
        return 0;
    }

    //查询所有收藏商品并返回商品+图片List
    @RequestMapping("/selectCollectDetail")
    public List selectCollectDetail(Integer userid) {
        Map<String, Object> map = new HashMap<>();
        List finalList = new ArrayList();
        List<Collect> collects = collectService.selectProductByUserid(userid);
        for (Collect collectTemp : collects) {
            if (!collects.isEmpty()){
                Product product = productService.selectProductWithImgF(collectTemp.getProductId());
                if(product != null && product.getStatus() == 1) {
                	finalList.add(product);
                }
            }
        }
        return finalList;
    }

    //查询用户是否为VIP
    @RequestMapping("/isVip")
    public Integer isVip(Integer userid){
        User user = userService.selectByPrimaryKey(userid);
        if(user != null && user.getIsVip() == 1){
            return 1;
        }else{
            return 0;
        }
    }
    
    /**
     * 查询优惠券和商品运费
     * @param userid
     * @param productid
     * @return
     */
    @RequestMapping("/selectFreightAndCoupons")
    public Map<String, Object> selectFreightAndCoupons(Integer userid, Integer productid){
    	Map<String, Object> map = userService.selectFreightAndCoupons(userid, productid);
    	return map;
    }
//	分享获得奖金
	@RequestMapping("/share")
	public void share(Integer uwea) {
//		userService.share(uwea);
	}
//	分享获得奖金
	@RequestMapping("/jiangjin")
	public Map jiangjin() {
		return userService.jiangjin();
	}
//	分享人数
	@RequestMapping("/shareNum")
	public int shareNum(Integer uuu) {
		return userService.shareNum(uuu);
	}
//	获取分享剩余的积分
	@RequestMapping("/shareScore")
	public int shareScore(Integer uuu) {
		return userService.shareScore(uuu);
	}
}
