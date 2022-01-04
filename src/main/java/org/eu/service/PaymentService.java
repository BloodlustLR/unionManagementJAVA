package org.eu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.eu.entity.*;

import java.util.List;
import java.util.Map;

public interface PaymentService extends IService<Payment> {


    IPage<Payment> pagePayment(Page<Payment> page,String startDate,String endDate);

    String addPayment(Payment payment, List<Integer> standardList);

    String configPayment(Payment payment, List<Integer> standardList);

    List<Ship> listPaymentShip(Integer paymentId);

    List<Union> getPaymentUnionArmy(Integer pid);

    Long getPaymentTotal(Integer pid);

    List<StandardPayment> getPaymentStandardPayment(Integer pid);

    Map<String,Object> getPaymentList(Integer pid,Integer pageNum,Integer pageSize);
}
