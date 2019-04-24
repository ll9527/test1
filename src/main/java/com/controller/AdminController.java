package com.controller;

import com.dao.FileOperation;
import com.entity.AdminImg;
import com.entity.AdminProfit;
import com.entity.Product;
import com.entity.Seller;
import com.entity.SellerBcImg;
import com.entity.User;
import com.service.AdminProfitService;
import com.service.ProductService;
import com.service.SellerAddressService;
import com.service.SellerBcImgService;
import com.service.SellerService;
import com.service.ShopOrderService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired(required = false)
    private SellerService sellerService;
    @Autowired(required = false)
    private UserService userService;
    @Autowired(required = false)
    private SellerBcImgService sellerBcImgService;
    @Autowired(required = false)
    private SellerAddressService sellerAddressService;
    @Autowired(required = false)
    private ShopOrderService shopOrderService;
    @Autowired(required = false)
    private AdminProfitService adminProfitService;
    @Autowired(required = false)
    private ProductService productService;

    //查询未批示商户信息
    @RequestMapping("/selectWtihoutConfirm")
    public List<User> selectWtihoutConfirm() {
        List<User> userList = userService.selectUnconfirmed();
        return userList;
    }

    //查询为批示商户信息详情
    @RequestMapping("/sellersMessage")
    public Seller sellersMessage(Integer sellerid) {
        return sellerService.selectUnconfirmDetial(sellerid);
    }

    //回执操作
    @RequestMapping("/sellerRecipt")
    public Map sellerRecipt(User user, Integer operation, Integer sellerid) {
        Map<String, Object> map = new HashMap<>();
        if (operation == 1) {
            //批准注册
            user.setIsSeller(1);
            user.setAppliedMark("");
            int isOk = userService.updateByPrimaryKeySelective(user);
            if (isOk > 0) {
                map.put("status", "ok");
                map.put("info", 1);
            } else {
                map.put("status", "OPERATIONno");
                map.put("info", 0);
            }
        } else if (operation == 0) {
            //更改isApply数据
            user.setIsApply(0);
            int isOk2 = userService.updateByPrimaryKeySelective(user);
            if (isOk2 > 0) {
                //删除sellerAddress数据
                int isOk3 = sellerAddressService.deleteBySellerId(sellerid);
                if (isOk3 > 0) {
                    //删除seller数据
                    int isOk4 = sellerService.deleteByPrimaryKey(sellerid);
                    if (isOk4 > 0) {
                        //删除磁盘文件
                        List<SellerBcImg> sellerBcImgList = sellerBcImgService.selectByUserid(user.getId());
                        Boolean flag = false;
                        for (SellerBcImg imgList : sellerBcImgList) {
                            String imgName = imgList.getImg();
                            FileOperation fileOperation = new FileOperation();
                            flag = fileOperation.SellerBcImgDelete(imgName);
                        }
                        if (flag) {
                            //删除sellerBcImg数据库资料
                            int isOk5 = sellerBcImgService.deleteByUserid(user.getId());
                            if (isOk5 > 0) {
                                map.put("status", "ok");
                                map.put("info", 1);
                            } else {
                                map.put("status", "IMGno");
                                map.put("info", 0);
                            }
                        } else {
                            map.put("status", "IOno");
                            map.put("info", 0);
                        }

                    } else {
                        map.put("status", "SELLERno");
                        map.put("info", 0);
                    }
                } else {
                    map.put("status", "SELLERADDRESSNO");
                    map.put("info", 0);
                }
            } else {
                map.put("status", "ISAPPLYno");
                map.put("info", 0);
            }
        }
        return map;
    }

    @RequestMapping("/isAdmin")
    public Integer checkIsAdmin(Integer userid){
        User user = userService.selectByPrimaryKey(userid);
        return user.getIsAdmin();
    }
//    查询公告
    @RequestMapping("/selectGG")
    public String selectGG(){
    	return userService.selectGG();
    }
//	平台管理页面查询
    @RequestMapping("/selectAllUserOrVip")
    public Map selectAllUserOrVip(){
    	Map map = new HashMap();
    	Integer vip = userService.selectVipUser();
    	Integer user = userService.selectAllUser();
    	BigDecimal sales = shopOrderService.selectSales();
    	BigDecimal allSales = shopOrderService.selectAllSales();
    	AdminProfit adminProfit = adminProfitService.selectAdmin();
    	BigDecimal activityBonus = adminProfit.getDiscountAmount();
    	BigDecimal rewardPool = adminProfit.getBonusPools();
    	map.put("vip", vip);
    	map.put("user", user);
    	map.put("sales", sales==null?0:sales);
    	map.put("allSales", allSales==null?0:allSales);
    	map.put("activityBonus", activityBonus==null?0:activityBonus);
    	map.put("rewardPool", rewardPool==null?0:rewardPool);
    	return map;
    }
//    抽活动奖设置
    @RequestMapping("/upPoint")
    public Integer upPoint(Integer sta, String point) {
    	if(point != null) {
    		return adminProfitService.upPoint(sta, point);
    	}
    	return 0;
    }
//    获得管理员设置
    @RequestMapping("/selectAP")
    public AdminProfit selectAP() {
    	return adminProfitService.selectAdmin();
    }
//    查询所有商家
    @RequestMapping("/selectAllSeller")
    public List<Seller> selectAllSeller() {
    	return sellerService.selectAll();
    }
//    更新公告
    @RequestMapping("/upAdminP")
    public void upAdminP(AdminProfit admin) {
    	admin.setId(1);
    	adminProfitService.updateByPrimaryKeySelective(admin);
    }
//    查看平台证件照
    @RequestMapping("/adminZImg")
    public List<AdminImg> adminZImg() {
    	return adminProfitService.selectAdminZImg();
    }
//    查看平台广告照
    @RequestMapping("/adminGGImg")
    public List<AdminImg> adminGGImg() {
    	return adminProfitService.selectAdminGGImg();
    }
//  查看平台广告照
	@RequestMapping("/adminShareImg")
	public List<AdminImg> adminShareImg() {
		return adminProfitService.selectAdminShareImg();
	}
//    平台照片上传
    @RequestMapping("/upload")
    public void upload(MultipartFile image, int sta) {
    	String ImgName = FileOperation.adminAddImg(image);
    	if(!ImgName.equals("error")) {
    		AdminImg adminImg = new AdminImg();
    		adminImg.setName(ImgName);
    		adminImg.setRole(String.valueOf(sta));
    		adminProfitService.inserAdminImg(adminImg);
    	}
    }
//    根据状态码删除平台照片
    @RequestMapping("/deleteImg")
    public void deleteImg(int sta) {
    	if(sta == 2) {
    		List<AdminImg> adminImgList = adminProfitService.selectAdminGGImg();
    		if(!adminImgList.isEmpty()) {
    			for (AdminImg adminImg : adminImgList) {
    				adminImg.setRole("-2");
    				adminProfitService.updataAdminImg(adminImg);
				}
    		}
    	}else if(sta == 3){
    		List<AdminImg> adminImgList = adminProfitService.selectAdminShareImg();
    		if(!adminImgList.isEmpty()) {
    			for (AdminImg adminImg : adminImgList) {
    				adminImg.setRole("-3");
    				adminProfitService.updataAdminImg(adminImg);
				}
    		}
    	}
    }
//    删除平台证件照
    @RequestMapping("/deleteZImg")
    public int deleteZImg(int imgId) {
    	return adminProfitService.deleteZImg(imgId);
    }
//    查看平台所有商品
    @RequestMapping("/allProduct")
    public List allProduct() {
    	return productService.selectAllProduct();
    }
}