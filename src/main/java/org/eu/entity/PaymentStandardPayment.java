package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("payment_standard_payment")
public class PaymentStandardPayment {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer paymentId;

    private Integer standardPaymentId;

    private String state;

}
