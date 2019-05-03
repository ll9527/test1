package com.serviceImp;

import com.dao.*;
import com.entity.AdminProduct;
import com.entity.Product;
import com.entity.ProductCommentFreight;
import com.entity.Seller;
import com.entity.SellerAddress;
import com.entity.ShopProductSpecification;
import com.entity.Version;
import com.entity.Version1;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImple implements ProductService {

    @Autowired(required = false)
    private ProductMapper productMapper;
    @Autowired(required = false)
    private VersionMapper versionMapper;
    @Autowired(required = false)
    private SellerMapper sellerMapper;
    @Autowired(required = false)
    private SellerWithProductImgMapper sellerWithProductImgMapper;
    @Autowired(required = false)
    private SellerAddressMapper sellerAddressMapper;
    @Autowired(required = false)
    private ProductCommentFreightMapper pcfm;
    @Autowired(required = false)
    private Version1Mapper v1m;
    @Autowired(required = false)
    private ShopProductSpecificationMapper spsm;
    @Autowired(required = false)
    private AdminProductMapper adminProductMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Product record) {
        return productMapper.insert(record);
    }

    @Override
    public int insertSelective(Product record) {
        return productMapper.insertSelective(record);
    }

    @Override
    public Product selectByPrimaryKey(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Product record) {
        return productMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Product record) {
        return productMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Product> selectAll() {
        return productMapper.selectAll();
    }

    @Override
    public int selectKey(Product record) {
        return productMapper.selectKey(record);
    }

    @Override
    public Map selectProductDetail(Integer productid) {
        Map map = new HashMap();
        //查询商品详情+所有图片
        List<Product> productList = productMapper.selectProductWithImg(productid);
        map.put("productList", productList);
        //查询商品型号
        List<ShopProductSpecification> versionList = spsm.selectLevel1Speci(productid);
        map.put("productVersion", versionList);
        //查询seller
        Integer sellerid = sellerWithProductImgMapper.selectByPid(productid).get(0).getSellerId();
        Seller seller = sellerMapper.selectByPrimaryKey(sellerid);
        map.put("sellerid", sellerid);
        map.put("sellerTitleName", seller.getTitleName());
        //查询sellerAddress
        List<SellerAddress> addressList = sellerAddressMapper.selectBySellerid(sellerid);
        map.put("addressList", addressList);
		// 查询商品分类（颜色）
		List<ShopProductSpecification> version1List = spsm.selectLevel2Speci(productid);
		map.put("productVersion1", version1List);
		
        List<ShopProductSpecification> skuList = spsm.selectSku(productid);
        map.put("skuList", skuList);
        return map;
    }
    
    @Override
    public Map selectAdminProDetail(Integer productid) {
        Map map = new HashMap();
        //查询商品详情+所有图片
        List<Product> productList = productMapper.selectProductWithImg(productid);
        map.put("productList", productList);
        //查询商品型号
        List<ShopProductSpecification> versionList = spsm.selectLevel1Speci(productid);
        map.put("productVersion", versionList);
        //查询seller
//        Integer sellerid = sellerWithProductImgMapper.selectByPid(productid).get(0).getSellerId();
//        Seller seller = sellerMapper.selectByPrimaryKey(sellerid);
//        map.put("sellerid", sellerid);
//        map.put("sellerTitleName", seller.getTitleName());
        //查询sellerAddress
//        List<SellerAddress> addressList = sellerAddressMapper.selectBySellerid(sellerid);
//        map.put("addressList", addressList);
		// 查询商品分类（颜色）
		List<ShopProductSpecification> version1List = spsm.selectLevel2Speci(productid);
		map.put("productVersion1", version1List);
		
        List<ShopProductSpecification> skuList = spsm.selectSku(productid);
        map.put("skuList", skuList);
        return map;
    }

    @Override
    public Product selectProductWithImgF(Integer productid) {
        return productMapper.selectProductWithImgF(productid);
    }

    @Override
    public List<Product> selectLevel1P(Integer classid) {
        return productMapper.selectLevel1P(classid);
    }

    @Override
    public List<Product> selectLevel2P(Map<String, Object> map) {
        return productMapper.selectLevel2P(map);
    }

    @Override
    public List<Product> selectFromSeller(Integer sellerid) {
        return productMapper.selectFromSeller(sellerid);
    }

    @Override
    public //全局模糊查询
    List<Product> serchProduct(Map<String, Object> map) {
        return productMapper.serchProduct(map);
    }

	@Override
	public Integer setPFreight(ProductCommentFreight pcf) {
		return pcfm.insertSelective(pcf);
	}

	@Override
	public List<ProductCommentFreight> selectCommentsByPid(Integer pId) {
		return pcfm.selectCommentsByPid(pId);
	}

	@Override
	public List selectAllProduct() {
		return productMapper.selectAllProduct();
	}

	@Override
	public List selectActivityPro(Integer sta) {
		// TODO Auto-generated method stub
		List<AdminProduct> adminProductList = adminProductMapper.selectProBySta(sta);
		if(!adminProductList.isEmpty()) {
			List list = new ArrayList<>();
			for (AdminProduct adminProduct : adminProductList) {
				Map map = new HashMap<>();
				Product product = productMapper.selectAdminProductByKey(adminProduct.getProductId());
				map.put("adminProduct", adminProduct);
				map.put("product", product);
				list.add(map);
			}
			return list;
		}
		
		return null;
	}
	/**
	 * 下架平台的活动商品
	 */
	@Override
	public Integer productDown(Integer productid) {
		// TODO Auto-generated method stub
		AdminProduct adminProduct = adminProductMapper.selectByPrimaryKey(productid);
		adminProduct.setStatus(0);
		return adminProductMapper.updateByPrimaryKey(adminProduct);
	}


}
