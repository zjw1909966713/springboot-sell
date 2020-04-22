package com.imooc.service.impl;

import com.imooc.exception.SellException;
import com.imooc.service.RedisLock;
import com.imooc.service.SecKillService;
import com.imooc.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 张进文
 * @ClassName SecKillServiceImpl
 * @Description TODO
 * @Date 2019/3/13 15:39
 * @Version 1.0
 */
@Service
@Slf4j
public class SecKillServiceImpl implements SecKillService{

    @Autowired
    private RedisLock redisLock;

    /**
     * @description: TODO
     * @author: 张进文
     * @param:
     * @return:
     * @date: 2019/3/13 15:51
     * @version: 1.0
     */

    static Map<String,Integer> products;
    static Map<String,Integer> stock;
    static Map<String,String> orders;

    static {
        products=new HashMap<>();
        stock=new HashMap<>();
        orders=new HashMap<>();
        products.put("123456",100000);
        stock.put("123456",100000);
    }

    private String queryMap(String productId){
        return "国庆活动，商品特价，限量"+products.get(productId)+",还剩："+stock.get(productId)+" 份，该商品成功下单用户数目："+orders.size()+"人";
    }




    @Override
    public String querySecKillProductInfo(String productId) {
        return this.queryMap(productId);
    }

    @Override
    public  void orderProductMockDiffUser(String productId) {

        long time=System.currentTimeMillis()+10*1000;


      //加锁
        boolean lockFlag = redisLock.lock(productId, String.valueOf(time));
        log.info("lockFlag====================="+lockFlag);

        if(!lockFlag){
            throw  new SellException(103,"太火爆了，重新下单");
        }

        //1.查询库存
        int stockNum=stock.get(productId);
        if(stockNum==0){
            throw  new SellException(100,"活动结束");
        }else{
            //2.下单
            orders.put(KeyUtil.genUniqueKey(),productId);
            //3.减库存
            stockNum--;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock.put(productId,stockNum);

        }

        //解锁
        redisLock.unlock(productId,String.valueOf(time));

    }


   /* @Override
    public synchronized void orderProductMockDiffUser(String productId) {




        //1.查询库存
        int stockNum=stock.get(productId);
        if(stockNum==0){
            throw  new SellException(100,"活动结束");
        }else{
            //2.下单
            orders.put(KeyUtil.genUniqueKey(),productId);
            //3.减库存
            stockNum--;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock.put(productId,stockNum);

        }

    }*/
}
