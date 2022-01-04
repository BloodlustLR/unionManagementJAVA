package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.eu.entity.*;

import java.util.List;

@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {

    IPage<Payment> pagePayment(Page<Payment> page,String startDate,String endDate);

    void insertPayment(Payment payment);

    List<Ship> listPaymentShip(Integer paymentId);

    List<Army> getPaymentUnionArmy(Integer paymentId);

    List<StandardPayment> listPaymentStandardPayment(Integer pid);
}
