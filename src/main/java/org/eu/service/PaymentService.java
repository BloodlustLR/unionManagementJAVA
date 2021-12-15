package org.eu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.eu.entity.Payment;
import org.eu.entity.Ship;

import java.util.List;

public interface PaymentService extends IService<Payment> {


    IPage<Payment> pagePayment(Page<Payment> page,String startDate,String endDate);

    String addPayment(Payment payment, List<Integer> standardList);

    List<Ship> listPaymentShip(Integer paymentId);
}
