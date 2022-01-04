package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("payment")
public class Payment {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;

    private Float rate;

    @TableField(exist=false)
    private List<StandardPayment> standardPaymentList;

    private String lossStartTime;

    private String lossEndTime;

    private String limitArea;

    private String limitConstellation;

    private String limitGalaxy;

    private String endTime;

    private String state;

    private String createTime;


}
