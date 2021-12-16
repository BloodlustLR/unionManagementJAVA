package org.eu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.*;
import org.eu.mapper.LossMapper;
import org.eu.mapper.PaymentMapper;
import org.eu.mapper.PaymentStandardPaymentMapper;
import org.eu.service.PaymentService;
import org.eu.service.PaymentStandardPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<Ship> standardShipList = paymentMapper.listPaymentShip(pid);

        QueryWrapper<Loss> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("payment_id",pid)
                .eq("state","A");
        List<Loss> lossList = lossMapper.selectList(queryWrapper);

        Long total = new Long(0);
        for(Loss loss:lossList){
            for(Ship standardShip :standardShipList){
                if(standardShip.getId().equals(loss.getShipId())){
                    total+=standardShip.getPrice();
                    break;
                }
            }
        }
        return total;
    }

}
