package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("payment")
public class Payment {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;

    private String lossStartTime;

    private String lossEndTime;

    private String limitArea;

    private String limitConstellation;

    private String limitGalaxy;

    private String endTime;

    private String state;

    private String createTime;


}
