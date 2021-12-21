package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("kill_report")
public class KillReport {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;

    private String killStartTime;

    private String killEndTime;

    private String limitArea;

    private String limitConstellation;

    private String limitGalaxy;

    private String endTime;

    private String state;

    private String createTime;


}
