package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @TableField(exist=false)
    private String armyName;

    @TableField(exist=false)
    private String armyShortName;

    private Integer shipId;

    @TableField(exist=false)
    private String shipName;

    @TableField(exist=false)
    private String shipType;

    private String area;

    private String constellation;

    private String galaxy;

    private Long num;

    private Long price;

    private String lossTime;

    private String kmShip;

    private String highAtkShip;

    private String img;

    private Boolean isModify;

    private String state;

}
