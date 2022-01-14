package org.eu.entity;

import com.baomidou.mybatisplus.annotation.*;
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

    @TableField(value = "loss_start_time", updateStrategy = FieldStrategy.IGNORED)
    private String lossStartTime;

    @TableField(value = "loss_end_time", updateStrategy = FieldStrategy.IGNORED)
    private String lossEndTime;

    @TableField(value = "limit_area", updateStrategy = FieldStrategy.IGNORED)
    private String limitArea;

    @TableField(value = "limit_constellation", updateStrategy = FieldStrategy.IGNORED)
    private String limitConstellation;

    @TableField(value = "limit_galaxy", updateStrategy = FieldStrategy.IGNORED)
    private String limitGalaxy;

    private String endTime;

    private String state;

    private String createTime;


}
