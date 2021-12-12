package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("standard_payment_ship")
public class StandardPaymentShip {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer standardPaymentId;

    private Integer shipId;

    @TableField(exist=false)
    private Long price;


}
