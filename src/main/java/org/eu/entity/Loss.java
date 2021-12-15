package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("loss")
public class Loss {

    @TableId(value = "id")
    private String id;

    private Integer paymentId;

    private Integer armyId;

    private Integer shipId;

    private String area;

    private String constellation;

    private String galaxy;

    private Long num;

    private String lossTime;

    private String kmShip;

    private String highAtkShip;

    private String img;

    private String state;

}
