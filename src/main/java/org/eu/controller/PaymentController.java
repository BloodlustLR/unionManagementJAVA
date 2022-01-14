package org.eu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.eu.entity.*;
import org.eu.service.LossService;
import org.eu.service.PaymentService;
import org.eu.service.PaymentStandardPaymentService;
import org.eu.util.DateFormatUtil;
import org.eu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    LossService lossService;

    @Autowired
    PaymentStandardPaymentService paymentStandardPaymentService;

    @PostMapping("/pagePayment")
    public IPage<Payment> pagePayment(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        List<String> dateRange = JSONObject.parseArray(strj.getString("dateRange"),String.class);

        String startDate=null;
        String endDate=null;
        if(dateRange!=null&&dateRange.size()!=0){
            startDate = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(dateRange.get(0)));
            endDate = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(dateRange.get(1)));
        }

        Integer pageNum = strj.getInteger("pageNum");
        Integer pageSize = strj.getInteger("pageSize");
        Page<Payment> page = new Page<>(pageNum,pageSize);

        return paymentService.pagePayment(page,startDate,endDate);
    }

    @GetMapping("/getPaymentInfo")
    public Payment getPaymentInfo(@RequestParam("pid") Integer pid){
        Payment payment = paymentService.getById(pid);
        List<StandardPayment> standardPaymentList = paymentService.getPaymentStandardPayment(pid);
        payment.setStandardPaymentList(standardPaymentList);
        return payment;
    }

    @GetMapping("/getPaymentTotal")
    public Map<String,Object> getPaymentTotal(@RequestParam("pid") Integer pid){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("priceTotal",paymentService.getPaymentTotal(pid));
        resultMap.put("lossTotal",lossService.getPaymentTotal(pid));
        return resultMap;
    }

    @GetMapping("/getPaymentUnionArmy")
    public List<Union> getPaymentUnionArmy(@RequestParam("pid") Integer pid){
        return paymentService.getPaymentUnionArmy(pid);
    }

    @GetMapping("/getPaymentShipList")
    public List<Ship> getPaymentShipList(@RequestParam("pid") Integer pid){
        return paymentService.listPaymentShip(pid);
    }

    @PostMapping("/getPaymentList")
    public Map<String,Object> getPaymentList(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);
        Integer pid = strj.getInteger("pid");
        Integer pageNum = strj.getInteger("pageNum");
        Integer pageSize = strj.getInteger("pageSize");
        return paymentService.getPaymentList(pid,pageNum,pageSize);
    }

    @PostMapping("/addPayment")
    public String addPayment(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Payment payment = new Payment();

        String name = strj.getString("name");
        String endTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(strj.getString("endTime")));
        Float rate = strj.getFloat("rate");
        payment.setName(name);
        payment.setRate(rate);
        payment.setEndTime(endTime);

        Boolean hasLimitTime = strj.getBoolean("hasLimitTime");
        if(hasLimitTime){
            List<String> limitTime = JSONObject.parseArray(strj.getString("limitTime"),String.class);
            String lossStartTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(limitTime.get(0)));
            String lossEndTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(limitTime.get(1)));
            payment.setLossStartTime(lossStartTime);
            payment.setLossEndTime(lossEndTime);
        }
        Boolean hasLimitArea = strj.getBoolean("hasLimitArea");
        if(hasLimitArea){
            String limitAreaStr = strj.getString("limitAreaList");
            payment.setLimitArea(limitAreaStr);
        }

        Boolean hasLimitConstellation = strj.getBoolean("hasLimitConstellation");
        if(hasLimitConstellation){
            String limitConstellationStr = strj.getString("limitConstellationList");
            payment.setLimitConstellation(limitConstellationStr);
        }

        Boolean hasLimitGalaxy = strj.getBoolean("hasLimitGalaxy");
        if(hasLimitGalaxy){
            String limitGalaxyStr = strj.getString("limitGalaxyList");
            payment.setLimitGalaxy(limitGalaxyStr);
        }

        List<Integer> standardList = JSONObject.parseArray(strj.getString("standardList"),Integer.class);

        return ResponseUtil.success(paymentService.addPayment(payment,standardList));
    }

    @PostMapping("/configPayment")
    public String configPayment(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Payment payment = new Payment();

        Integer id = strj.getInteger("id");
        String name = strj.getString("name");
        String endTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(strj.getString("endTime")));
        Float rate = strj.getFloat("rate");
        payment.setId(id);
        payment.setRate(rate);
        payment.setName(name);
        payment.setEndTime(endTime);

        Boolean hasLimitTime = strj.getBoolean("hasLimitTime");
        if(hasLimitTime){
            List<String> limitTime = JSONObject.parseArray(strj.getString("limitTime"),String.class);
            String lossStartTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(limitTime.get(0)));
            String lossEndTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(limitTime.get(1)));
            payment.setLossStartTime(lossStartTime);
            payment.setLossEndTime(lossEndTime);
        }else{
            payment.setLossStartTime(null);
            payment.setLossEndTime(null);
        }

        Boolean hasLimitArea = strj.getBoolean("hasLimitArea");
        if(hasLimitArea){
            String limitAreaStr = strj.getString("limitAreaList");
            payment.setLimitArea(limitAreaStr);
        }else{
            payment.setLimitArea(null);
        }

        Boolean hasLimitConstellation = strj.getBoolean("hasLimitConstellation");
        if(hasLimitConstellation){
            String limitConstellationStr = strj.getString("limitConstellationList");
            payment.setLimitConstellation(limitConstellationStr);
        }else{
            payment.setLimitConstellation(null);
        }

        Boolean hasLimitGalaxy = strj.getBoolean("hasLimitGalaxy");
        if(hasLimitGalaxy){
            String limitGalaxyStr = strj.getString("limitGalaxyList");
            payment.setLimitGalaxy(limitGalaxyStr);
        }else{
            payment.setLimitGalaxy(null);
        }

        List<Integer> standardList = JSONObject.parseArray(strj.getString("standardList"),Integer.class);

        return ResponseUtil.success(paymentService.configPayment(payment,standardList));
    }

    @PostMapping("/removePayment")
    public String removePayment(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Integer id = strj.getInteger("id");

        Boolean flag = paymentService.removeById(id);

        return ResponseUtil.success(flag?"success":"fail");
    }


}
