package com.imooc.controller;

import com.imooc.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张进文
 * @ClassName SecKillController
 * @Description TODO
 * @Date 2019/3/13 15:37
 * @Version 1.0
 */
@RestController
@RequestMapping("/skill")
@Slf4j
public class SecKillController {


    @Autowired
    private SecKillService secKillService;


    /**
     * @description: 查询秒杀活动商品的特价信息
     * @author: 张进文
     * @param: [productId]
     * @return: java.lang.String
     * @date: 2019/3/13 15:43
     * @version: 1.0
     */
    @GetMapping("/query/{productId}")
    public String query(@PathVariable String productId) throws  Exception{
        return secKillService.querySecKillProductInfo(productId);

    }


    @GetMapping("/order/{productId}")
    public String skill(@PathVariable String productId) throws  Exception{
        log.info("@skill request,productId:"+productId);
        secKillService.orderProductMockDiffUser(productId);

        return secKillService.querySecKillProductInfo(productId);

    }





}
