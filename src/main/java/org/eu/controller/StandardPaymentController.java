package org.eu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.eu.entity.StandardPayment;
import org.eu.entity.StandardPaymentShip;
import org.eu.service.StandardPaymentService;
import org.eu.service.StandardPaymentShipService;
import org.eu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/standardPayment")
public class StandardPaymentController {

    @Autowired
    StandardPaymentService standardPaymentService;

    @Autowired
    StandardPaymentShipService standardPaymentShipService;


    @GetMapping("/getAllStandardPayment")
    public List<StandardPayment> getAllStandardPayment(){
        return standardPaymentService.getAllStandardPayment();
    }

    @PostMapping("/addStandardPayment")
    public String addStandardPayment(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        StandardPayment standardPayment = new StandardPayment();
        standardPayment.setName(strj.getString("name"));
        standardPayment.setNum(new Long(10000));

        Boolean result = standardPaymentService.save(standardPayment);

        return ResponseUtil.success(result?"success":"fail");
    }

    @PostMapping("/configStandardPayment")
    public String configStandardPayment(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        StandardPayment standardPayment = new StandardPayment();
        standardPayment.setId(strj.getInteger("id"));
        standardPayment.setNum(strj.getLong("num"));

        List<Integer> shipIdList = JSONObject.parseArray(strj.getString("shipList"),Integer.class);

        QueryWrapper<StandardPaymentShip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("standard_payment_id",standardPayment.getId());
        standardPaymentShipService.remove(queryWrapper);

        List<StandardPaymentShip> standardPaymentShipList = new ArrayList<>();
        for(Integer shipId : shipIdList){
            StandardPaymentShip standardPaymentShip = new StandardPaymentShip();
            standardPaymentShip.setShipId(shipId);
            standardPaymentShip.setStandardPaymentId(standardPayment.getId());
            standardPaymentShipList.add(standardPaymentShip);
        }
        standardPaymentShipService.saveBatch(standardPaymentShipList);

        Boolean result = standardPaymentService.updateById(standardPayment);

        return ResponseUtil.success(result?"success":"fail");
    }

    @PostMapping("/removeStandardPayment")
    public String removeStandardPayment(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Integer standardId = strj.getInteger("id");

        Boolean flag = standardPaymentService.removeById(standardId);

        return ResponseUtil.success(flag?"success":"fail");
    }

}
