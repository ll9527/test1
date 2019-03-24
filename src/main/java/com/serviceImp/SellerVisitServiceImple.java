package com.serviceImp;

import com.dao.SellerVisitMapper;
import com.dao.SellerWithProductImgMapper;
import com.entity.Seller;
import com.entity.SellerVisit;
import com.entity.SellerWithProductImg;
import com.service.SellerVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SellerVisitServiceImple implements SellerVisitService {

    @Autowired(required = false)
    private SellerVisitMapper sellerVisitMapper;
    @Autowired(required = false)
    private SellerWithProductImgMapper sellerWithProductImgMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return sellerVisitMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SellerVisit record) {
        return sellerVisitMapper.insert(record);
    }

    @Override
    public int insertSelective(SellerVisit record) {
        return sellerVisitMapper.insertSelective(record);
    }

    @Override
    public SellerVisit selectByPrimaryKey(Integer id) {
        return sellerVisitMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SellerVisit record) {
        return sellerVisitMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SellerVisit record) {
        return sellerVisitMapper.updateByPrimaryKey(record);
    }

    @Override
    public int insertVistNum(Map<String, Integer> map) {
        Integer isOk = 0;
        SellerVisit sellerVisit = new SellerVisit();
        //如果sellerid存在，则获取并插入数据，如果productid存在，则通过swp表查询sellerid
        if (map.get("sellerid") != null) {
            //如果sellerid存在，则更新vistNum字段
            sellerVisit = sellerVisitMapper.selectBySellerid(map.get("sellerid"));
            if (sellerVisit != null) {
                sellerVisit.setVisitNum(sellerVisit.getVisitNum() + 1);
                isOk = sellerVisitMapper.updateByPrimaryKeySelective(sellerVisit);
            } else {
                //否则插入新字段
                SellerVisit newSellerVist = new SellerVisit();
                newSellerVist.setSellerId(map.get("sellerid"));
                newSellerVist.setVisitNum((long) 0);
                newSellerVist.setVisitNum(newSellerVist.getVisitNum() + 1);
                isOk =  sellerVisitMapper.insertSelective(newSellerVist);
            }
        } else if (map.get("productid") != null) {
            //查询swp关联List
            List<SellerWithProductImg> list = sellerWithProductImgMapper.selectByPid(map.get("productid"));
            Integer sellerid = list.get(0).getSellerId();
            //如果sellerid存在，则更新vistNum字段
            sellerVisit = sellerVisitMapper.selectBySellerid(sellerid);
            if (sellerVisit != null) {
                sellerVisit.setVisitNum(sellerVisit.getVisitNum() + 1);
                isOk =  sellerVisitMapper.updateByPrimaryKeySelective(sellerVisit);
            } else {
                //否则插入新字段
                SellerVisit newSellerVist = new SellerVisit();
                newSellerVist.setSellerId(sellerid);
                newSellerVist.setVisitNum((long) 0);
                newSellerVist.setVisitNum(newSellerVist.getVisitNum() + 1);
                isOk =  sellerVisitMapper.insertSelective(newSellerVist);
            }
        }
        return isOk;
    }
    
    //定时清除每日人数
    @Override
    public void clearSellerVisit(){
    	//定时器 零点清空访问人数
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("启动定时器");
                List<SellerVisit> sellerVisits = sellerVisitMapper.selectAll();
                //循环更新sellerVist所有数据
                for (SellerVisit vistTemp: sellerVisits ) {
                    vistTemp.setVisitNum((long)0);
                    sellerVisitMapper.updateByPrimaryKeySelective(vistTemp);
                }
            }
        };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date =calendar.getTime();
        timer.schedule( task , date);
    }

    @Override
    public SellerVisit selectBySellerid(Integer sellerid) {
        return sellerVisitMapper.selectBySellerid(sellerid);
    }

    @Override
    public List<SellerVisit> selectAll() {
        return sellerVisitMapper.selectAll();
    }
}
