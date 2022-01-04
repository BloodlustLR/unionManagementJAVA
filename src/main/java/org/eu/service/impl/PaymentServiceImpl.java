package org.eu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import org.eu.entity.*;
import org.eu.mapper.LossMapper;
import org.eu.mapper.PaymentMapper;
import org.eu.mapper.PaymentStandardPaymentMapper;
import org.eu.service.PaymentService;
import org.eu.service.PaymentStandardPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    @Autowired
    PaymentMapper paymentMapper;

    @Autowired
    LossMapper lossMapper;

    @Autowired
    PaymentStandardPaymentService paymentStandardPaymentService;

    @Override
    public IPage<Payment> pagePayment(Page<Payment> page,String startDate,String endDate) {

        return paymentMapper.pagePayment(page,startDate,endDate);
    }

    @Override
    public String addPayment(Payment payment,List<Integer> standardList) {

        paymentMapper.insertPayment(payment);

        List<PaymentStandardPayment> paymentStandardPaymentList = new ArrayList<>();
        for(Integer standardId:standardList){
            PaymentStandardPayment paymentStandardPayment = new PaymentStandardPayment();
            paymentStandardPayment.setPaymentId(payment.getId());
            paymentStandardPayment.setStandardPaymentId(standardId);
            paymentStandardPaymentList.add(paymentStandardPayment);
        }
        Boolean flag = paymentStandardPaymentService.saveBatch(paymentStandardPaymentList);

        return flag?"success":"fail";
    }

    @Override
    public String configPayment(Payment payment, List<Integer> standardList) {

        paymentMapper.updateById(payment);

        QueryWrapper<PaymentStandardPayment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("payment_id",payment.getId());
        paymentStandardPaymentService.remove(queryWrapper);
        List<PaymentStandardPayment> paymentStandardPaymentList = new ArrayList<>();
        for(Integer standardId:standardList){
            PaymentStandardPayment paymentStandardPayment = new PaymentStandardPayment();
            paymentStandardPayment.setPaymentId(payment.getId());
            paymentStandardPayment.setStandardPaymentId(standardId);
            paymentStandardPaymentList.add(paymentStandardPayment);
        }
        Boolean flag = paymentStandardPaymentService.saveBatch(paymentStandardPaymentList);
        return flag?"success":"fail";
    }

    @Override
    public List<Ship> listPaymentShip(Integer paymentId) {
        return paymentMapper.listPaymentShip(paymentId);
    }


    @Override
    public List<Union> getPaymentUnionArmy(Integer paymentId) {

        List<Union> unionList = new ArrayList<>();

        List<Army> armyList = paymentMapper.getPaymentUnionArmy(paymentId);

        List<String> unionNameList = new ArrayList<>();
        for(Army army:armyList){
            if(!unionNameList.contains(army.getUnionName())){
                Union newUnion = new Union();
                newUnion.setName(army.getUnionName());
                newUnion.setArmyList(new ArrayList<>());
                unionList.add(newUnion);
                unionNameList.add(newUnion.getName());
            }
            army.setName("["+army.getShortName()+"]"+army.getName());
            unionList.get(unionNameList.indexOf(army.getUnionName())).getArmyList().add(army);
        }
        return unionList;
    }

    @Override
    public Long getPaymentTotal(Integer pid) {
        QueryWrapper<Loss> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("payment_id",pid)
                .eq("state","A");
        List<Loss> lossList = lossMapper.selectList(queryWrapper);

        Long total = new Long(0);
        for(Loss loss:lossList){
            if(loss.getPrice()!=null){
                total+=loss.getPrice();
            }
        }
        return total;
    }

    @Override
    public List<StandardPayment> getPaymentStandardPayment(Integer pid) {

        return paymentMapper.listPaymentStandardPayment(pid);
    }

    @Override
    public Map<String,Object> getPaymentList(Integer pid,Integer pageNum,Integer pageSize) {
        List<Map<String,Object>> resultList = new ArrayList<>();
        List<Loss> lossList = lossMapper.getPaymentLossWithUnionArmy(pid);

        List<StandardPayment> standardPaymentList = this.getPaymentStandardPayment(pid);

        List<String> hasContainedArmy = new ArrayList<>();

        for(Loss loss:lossList){
           String armyName = loss.getArmyName();
           String armyShortName = loss.getArmyShortName();
           Map<String,Object> arrangeMap = new HashMap<>();
           //有就找之前的，没有就新建
           if(!hasContainedArmy.contains(armyShortName)){
               arrangeMap.put("armyName",armyName);
               arrangeMap.put("armyShortName",armyShortName);
               arrangeMap.put("price",Long.valueOf(0));
               for(StandardPayment standardPayment : standardPaymentList){
                   arrangeMap.put(standardPayment.getName(),Integer.valueOf(0));
               }

               resultList.add(arrangeMap);
               hasContainedArmy.add(armyShortName);
           }else{
               for(Map<String,Object> item :resultList){
                   if(item.get("armyShortName").equals(armyShortName)){
                       arrangeMap = item;
                       break;
                   }
               }
           }

           String shipName = loss.getShipName();
           for(StandardPayment standardPayment : standardPaymentList){
               boolean findFlag = false;
               for(Ship ship : standardPayment.getRelatedShipList()){
                   if(ship.getName().equals(shipName)){

                       Long historyPrice = (Long) arrangeMap.get("price");
                       historyPrice+=loss.getPrice();

                       Integer historyCount = (Integer) arrangeMap.get(standardPayment.getName());
                       historyCount++;

                       arrangeMap.put("price",historyPrice);
                       arrangeMap.put(standardPayment.getName(),historyCount);

                       findFlag = true;
                       break;
                   }
               }
               if(findFlag){
                   break;
               }
           }

        }

        return pageHelper(resultList,pageNum,pageSize);
    }


    public <T> Map<String,Object> pageHelper(List<T> list,Integer pageNum,Integer pageSize){
        Map<String,Object> resultMap = new HashMap<>();


        Integer totalPage = list.size()/pageSize;
        Integer rest = list.size()%pageSize;
        if(rest>0){
            totalPage++;
        }

        if(totalPage==0){

        }else if(pageSize<totalPage){
            list = list.subList((pageNum-1)*pageSize,pageNum*pageSize);
        }else{
            pageNum = totalPage;
            list = list.subList((pageNum-1)*pageSize,list.size());
        }

        resultMap.put("total",list.size());
        resultMap.put("current",pageNum);
        resultMap.put("size",pageSize);
        resultMap.put("records",list);

        return resultMap;
    }

}
