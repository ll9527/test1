package com.controller;

import com.entity.Address;
import com.entity.SellerAddress;
import com.entity.User;
import com.service.AddressService;
import com.service.SellerAddressService;
import com.service.SellerService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * author:@洪伟
 *
 *
 *
 * */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired(required = false)
    private AddressService addressService;

    @Autowired(required = false)
    private SellerAddressService sellerAddressService;

    @Autowired(required = false)
    private UserService userService;

    @Autowired(required = false)
    private SellerService sellerService;


    @RequestMapping("/userAddress")
    public List<Address> userAddress(Integer userid) {
        List<Address> addressList = addressService.selectByUserId(userid);
        return addressList;
    }

    @RequestMapping("/sellerAddress")
    public List<SellerAddress> sellerAddresses(Integer sellerid) {
        List<SellerAddress> sellerAddressList = sellerAddressService.selectByUserId(sellerid);
        return sellerAddressList;
    }

    @RequestMapping("/addAddress")
    public Map addUserAddress(Integer userid, String address, String username) {
        Map<String, Object> map = new HashMap<>();
        User user = userService.selectByPrimaryKey(userid);
        if (user != null && user.getIsSeller() == 0) {
            Address userAddress = new Address();
            userAddress.setUserId(userid);
            userAddress.setAddress(address);
            userAddress.setUserName(username);
            int isOk = addressService.insertSelective(userAddress);
            if (isOk > 0) {
                map.put("status", "ok");
                map.put("info", 1);
            } else {
                map.put("status", "no");
                map.put("info", 0);
            }
        } else if (user.getIsSeller() == 1) {
            SellerAddress sellerAddress = new SellerAddress();
            sellerAddress.setSellerId(userid);
            sellerAddress.setAddress(address);
            sellerAddress.setSellerName(username);
            int isOk = sellerAddressService.insertSelective(sellerAddress);
            if (isOk > 0) {
                map.put("status", "ok");
                map.put("info", 1);
            } else {
                map.put("status", "no");
                map.put("info", 0);
            }
        }else{
                map.put("status", "no");
                map.put("info", -9);
        }
        return map;
    }

}
