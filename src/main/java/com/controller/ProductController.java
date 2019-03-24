package com.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dao.FileOperation;
import com.entity.*;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
/*
 * author:@洪伟
 *
 *
 * */

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private Version1Service version1Service;
    @Autowired
    private ProductImgService productImgService;
    //关联商品图片/商家
    @Autowired
    private SellerPimgService sellerPimgService;
    @Autowired
    private ClassWithProductService classWithProductService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private ShopProductSpecificationService spss;

    //通过sellerid查询商家所有上架商品+图片
    @RequestMapping("/selectPdBySellerid")
    public List<Seller> selectSellerDetail(Integer sellerid,Integer operation) {
        Map<String, Object> map = new HashMap<>();
        map.put("sellerid", sellerid);
        map.put("operation", operation);
        return sellerService.selectSellerDetail(map);
    }

    //通过seller查询已上架商品
    @RequestMapping("/selectFromSeller")
    public List<Product> selectFromSelelr(Integer sellerid) {
        return productService.selectFromSeller(sellerid);
    }

    @RequestMapping("/selectHotP")
    public List<Product> selectHotP() {
        List<Product> productList = productService.selectAll();
        return productList;
    }

    //弃用
    @RequestMapping("/selectLevel1Ptest")
    public List<Product> selectLevel1Ptest(Integer classid) {
        List<Shop> shopList = shopService.selectLevel1and2(classid);
        List list = new ArrayList();
        for (Shop shopTemp : shopList) {
            Integer level2Id = shopTemp.getTowLevelName().get(0).getClassId();
            list.add(productService.selectLevel1P(level2Id));
        }
        return list;
    }

    /**
     * 根据一级目录id查下面所有关联商品
     *
     * @param classid
     * @return
     */
    @RequestMapping("/selectLevel1P")
    public List<Product> selectLevel1P(Integer classid) {
        List list = new ArrayList();
        if(!(shopService.selectLevel1and2(classid).isEmpty())){
            Shop shop = shopService.selectLevel1and2(classid).get(0);
            for (Shop shopTemp : shop.getTowLevelName()) {
                Integer level2Id = shopTemp.getClassId();
//            根据2级目录id查所有关联商品，可能为空List
                List<Product> productList = productService.selectLevel1P(level2Id);
//            判断list是不是空
                if (!(productList.isEmpty())) {
                    for (Product product : productList) {
                        list.add(product);
                    }
                }
            }
        }else{
            return null;
        }
        return list;
    }

    @RequestMapping("/selectLevel2P")
    public List<Product> selectLevel2P(Integer classid, Integer operationCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("classid", classid);
        map.put("operationCode", operationCode);
        return productService.selectLevel2P(map);
    }

    //添加商品，做商品表和版本表操作
    @RequestMapping(value = "/insertP")
    public Map insertP(Product product, String versionList, String versionList1, String skuList, 
    		Integer allSku, Integer sellerid, Integer cid, Integer freight) {
        Map<String, Object> map = new HashMap<>();
        /*
        //若团购价格不为0则设置为团购商品
        if (product.getGroupPrice() == null) {
            product.setIsGroup(0);
        } else if (product.getGroupPrice().compareTo(BigDecimal.ZERO) != 0) {
            product.setIsGroup(1);
        } else {
            product.setIsGroup(0);
        }
         */
        product.setIsGroup(1);
        product.setStatus((byte) 1);
        product.setCreated(new Date());
        product.setUpdated(new Date());
        product.setSalesVolume(0);
        int isOk = productService.selectKey(product);
        if (isOk > 0) {
            //插入关联表
            ClassWithProduct classWithProduct = new ClassWithProduct();
            classWithProduct.setLevel2ClassId(cid);
            classWithProduct.setProductId(product.getId());
            classWithProductService.insertSelective(classWithProduct);
            SellerWithProductImg sellerWithProductImg = new SellerWithProductImg();
            sellerWithProductImg.setProductId(product.getId());
            sellerWithProductImg.setSellerId(sellerid);
            sellerPimgService.insertSelective(sellerWithProductImg);
            List<String> versions = JSON.parseArray(versionList, String.class);
            List<String> versions1 = JSON.parseArray(versionList1, String.class);
            List<Map<String, String>> spsList = JSONArray.parseObject(skuList, List.class);
            System.out.println(spsList);

            for (String versionItem : versions) {
            	ShopProductSpecification version = new ShopProductSpecification();
                version.setProductId(product.getId());
                version.setvId(0);
                version.setVersionName(versionItem);
                spss.insertSelective(version);
                for (String versionItem1 : versions1) {
                	ShopProductSpecification version1 = new ShopProductSpecification();
                	version1.setProductId(product.getId());
                	version1.setvId(version.getId());
                	version1.setVersionName(versionItem1);
                	spss.insertSelective(version1);
				}
			}
            Integer price = 0;
            Integer groupPrice = 0;
            Integer count = 0;
			for (Map<String, String> item : spsList) {
				ShopProductSpecification specification = new ShopProductSpecification();
				specification.setProductId(product.getId());
				specification.setVersionName(item.get("version") + item.get("version1"));
				specification.setPrice(new BigDecimal(item.get("price")));
				specification.setGroupPrice(new BigDecimal(item.get("groupPrice")));
				Integer i = 0;
				if (item.get("num") != null) {
					i = Integer.valueOf(item.get("num"));
				}
				specification.setNum(i);
				spss.insertSelective(specification);
//				选出最小价钱，放来显示
				Integer itemPrice = Integer.valueOf(item.get("price"));
				Integer itemGroupPrice = Integer.valueOf(item.get("groupPrice"));
				if(count == 0) {
					price = itemPrice;
					groupPrice = itemGroupPrice;
				}else {
					if(itemPrice < price) {
						price = itemPrice;
					}
					if(itemGroupPrice < groupPrice) {
						groupPrice = itemGroupPrice;
					}
				}
				count++;
			}
//			插入最小价钱
			product.setPrice(new BigDecimal(price));
			product.setGroupPrice(new BigDecimal(groupPrice));
			product.setNum(1);
			productService.updateByPrimaryKeySelective(product);
            //插入运费
            ProductCommentFreight pcf = new ProductCommentFreight();
            pcf.setFreight(new BigDecimal(freight));
            pcf.setProductId(product.getId());
            productService.setPFreight(pcf);
            // 拼装返回的数据
            map.put("status", "yes");
            map.put("info", 1);
            map.put("productid", product.getId());
        } else {
            map.put("status", "no");
            map.put("info", -1);
        }
        return map;
    }

    //上传商品图片，做io操作和商品图片表操作
    @RequestMapping("/upload")
    public Map upload(MultipartFile image, HttpServletRequest request, Integer isCover, Integer productid) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        //添加商品图片
        request.setCharacterEncoding("UTF-8");
        FileOperation fileOperation = new FileOperation();
        String imgName = fileOperation.SellerBcImgAdd(image);
        if (!imgName.equals("error")) {
            //新增数据库
            ProductImg productImg = new ProductImg();
            productImg.setImage(imgName);
            if (isCover == 1) {
                productImg.setIsCover(1);
            } else {
                productImg.setIsCover(0);
            }
            productImg.setProductId(productid);
            productImgService.insertSelective(productImg);
        } else {
            map.put("status", "no");
            map.put("info", 0);
        }
        return map;
    }

    @RequestMapping("/selectDetail")
    public Map selectDetail(Integer productid) {
        return productService.selectProductDetail(productid);
    }

    @RequestMapping("/productDown")
    public Map productDown(Integer productid) {
        Map<String, Object> map = new HashMap<>();
        Product product = productService.selectByPrimaryKey(productid);
        if (product != null) {
            product.setStatus((byte) 0);
            productService.updateByPrimaryKey(product);
            map.put("status", "ok");
            map.put("info", 1);
        } else {
            map.put("status", "no");
            map.put("info", -1);
        }
        return map;
    }

    @RequestMapping("/serchProduct")
    public List<Product> serchProduct(String pname, @RequestParam(required = false)Integer operationCode) {
        Map<String, Object> map = new HashMap<>();
        if(operationCode != null) {
            map.put("operationCode", operationCode);
        }
        map.put("pname", pname);
        return productService.serchProduct(map);
    }
}
