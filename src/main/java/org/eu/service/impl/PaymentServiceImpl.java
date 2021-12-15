package org.eu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.Payment;
import org.eu.entity.PaymentStandardPayment;
import org.eu.entity.Ship;
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

}
