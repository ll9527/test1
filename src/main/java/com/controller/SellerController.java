package com.controller;

import com.dao.FileOperation;
import com.entity.*;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired(required = false)
    private SellerService sellerService;
    @Autowired(required = false)
    private UserService userService;
    @Autowired(required = false)
    private SellerBcImgService sellerBcImgService;
    @Autowired(required = false)
    private SellerAddressService sellerAddressService;
    @Autowired(required = false)
    private CollectService collectService;
    @Autowired(required = false)
    private SellerVisitService sellerVisitService;


    //提交入驻申请
    @RequestMapping(value = "/applyforSeller")
    public Map applyforSeller(Integer userid, @RequestParam(value = "applyMoney", defaultValue = "0") BigDecimal applyMoney, Seller seller, SellerAddress sellerAddress) {
        Map<String, Object> map = new HashMap<>();
        //查询User信息并修改is_seller字段
        User user = userService.selectByPrimaryKey(userid);
        if (user.getIsApply() == 0) {
            user.setIsApply(1);
            user.setApplyMoney(applyMoney);
            int isOk = userService.updateByPrimaryKeySelective(user);
            if (isOk > 0) {
                //插入Seller数据
                seller.setUserId(userid);
                System.out.println(seller.getSellerClass());
                int isOk2 = sellerService.selectKey(seller);
                if (isOk2 > 0) {
                    //插入sellerAddress
                    sellerAddress.setSellerName(user.getUsername());
                    sellerAddress.setSellerId(seller.getId());
                    int isOk3 = sellerAddressService.insertSelective(sellerAddress);
//                    插入创建每日人数表的数据
                    SellerVisit sv = new SellerVisit();
                    sv.setSellerId(seller.getId());
                    sv.setVisitNum((long) 0);
                    sellerVisitService.insertSelective(sv);
                    if (isOk3 > 0) {
                        map.put("status", "ok");
                        map.put("info", 1);
                    } else {
                        map.put("status", "no");
                        map.put("info", -1);
                    }
                } else {
                    map.put("status", "no");
                    map.put("info", -1);
                }
            } else {
                map.put("status", "no");
                map.put("info", -1);
            }
        } else if (user.getIsApply() == 1) {
            map.put("status", "no");
            map.put("info", -9);//-9为已提交注册，不予重新注册
        }
        return map;
    }
    
    /**
     * 商家注册，接收商家证件图片，和插入数据
     * @param userid
     * @param request
     * @param image
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/upload")
    public Map upload(Integer userid, HttpServletRequest request, @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        Map<String, Object> map = new HashMap<>();
        request.setCharacterEncoding("UTF-8");
        FileOperation fileOperation = new FileOperation();
        String imgName = fileOperation.SellerBcImgAdd(image);
        if (!imgName.equals("error")) {
            //新增数据库
            Seller seller = sellerService.selectByUserid(userid);
            SellerBcImg sellerBcImg = new SellerBcImg();
            sellerBcImg.setUserId(userid);
            sellerBcImg.setSellerId(seller.getId());
            sellerBcImg.setImg(imgName);
            int isOk = sellerBcImgService.insertSelective(sellerBcImg);
            if (isOk > 0) {
                map.put("status", "ok");
                map.put("info", 1);
            } else {
                map.put("status", "no");
                map.put("info", 0);
            }
        } else {
            map.put("status", "no");
            map.put("info", 0);
        }
        return map;
    }

    @RequestMapping("/isSeller")
    public Map checkIsSeller(Integer userid) {
        Map<String, Object> map = new HashMap<>();
        User user = userService.selectByPrimaryKey(userid);
        Seller seller = sellerService.selectByUserid(userid);
        if (seller == null) {
            map.put("info", -1);
        } else {
            map.put("isSeller", user.getIsSeller());
            map.put("sellerId", seller.getId());
        }
        return map;
    }

    //显示店铺分类+每个店铺热销前三
    @RequestMapping("/selectSellerClass")
    public List<Seller> selectSellerClass(String sellerClass) {
        //拿到此类别的所有商户
        List<Seller> sellerList = sellerService.selectSellerFromSellerClass(sellerClass);
        //遍历商户id并分别查询热销前三
        List finalList = new ArrayList();
        for (Seller sellerTemp : sellerList) {
            Integer sellerid = sellerTemp.getId();
            List<Seller> sellerAndProductList = sellerService.selectSellerTopThree(sellerid);
            if (!(sellerAndProductList.isEmpty())) {
                finalList.add(sellerAndProductList);
            }
        }
        return finalList;
    }

    @RequestMapping("/selectAllSeller")
    public List<Seller> selectAllSeller() {
        List<Seller> sellerList = sellerService.selectAll();
        List finalList = new ArrayList();
        for (Seller sellerTemp : sellerList) {
            Integer sellerid = sellerTemp.getId();
            List<Seller> sellerAndProductList = sellerService.selectSellerTopThree(sellerid);
            if (!(sellerAndProductList.isEmpty())) {
                finalList.add(sellerAndProductList);
            }
        }
        return finalList;
    }
    
    //定时器 零点清空访问人数
    @RequestMapping("/clearSellerVisit")
    public String clearSellerVisit() {
    	sellerVisitService.clearSellerVisit();
		return "ok";
    }

    //新增访问人数
    @RequestMapping("/addVisitNum")
    public Integer addVisitNum(@RequestParam(required = false) Integer sellerid, @RequestParam(required = false) Integer productid) {
        Map<String, Integer> map = new HashMap<>();
        map.put("productid", productid);
        map.put("sellerid", sellerid);
        int isOk = sellerVisitService.insertVistNum(map);
        if (isOk > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    //查询访客人数
    @RequestMapping("/getVisitNum")
    public Long getVisitNum(Integer sellerid){
        SellerVisit sellerVisit = sellerVisitService.selectBySellerid(sellerid);
        return sellerVisit.getVisitNum();
    }

    @RequestMapping("/getSImageList")
    public List<SellerBcImg> getSImageList(Integer sellerId){
    	return sellerService.selectBySId(sellerId);
    }

}

