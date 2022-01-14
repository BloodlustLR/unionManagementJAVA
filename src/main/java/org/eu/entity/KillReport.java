package org.eu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("kill_report")
public class KillReport {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;

    @TableField(value = "kill_start_time", updateStrategy = FieldStrategy.IGNORED)
    private String killStartTime;

    @TableField(value = "kill_end_time", updateStrategy = FieldStrategy.IGNORED)
    private String killEndTime;

    @TableField(value = "target_union", updateStrategy = FieldStrategy.IGNORED)
    private String targetUnion;

    @TableField(value = "target_army", updateStrategy = FieldStrategy.IGNORED)
    private String targetArmy;

    @TableField(value = "limit_area", updateStrategy = FieldStrategy.IGNORED)
    private String limitArea;

    @TableField(value = "limit_constellation", updateStrategy = FieldStrategy.IGNORED)
    private String limitConstellation;

    @TableField(value = "limit_galaxy", updateStrategy = FieldStrategy.IGNORED)
    private String limitGalaxy;

    private String endTime;

    private String state;

    private Boolean needDetail;

    private String createTime;


}
