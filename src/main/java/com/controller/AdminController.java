package com.controller;

import com.dao.FileOperation;
import com.entity.Seller;
import com.entity.SellerBcImg;
import com.entity.User;
import com.service.SellerAddressService;
import com.service.SellerBcImgService;
import com.service.SellerService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}