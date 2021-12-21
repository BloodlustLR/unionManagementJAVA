package org.eu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("`kill`")
public class Kill {

    @TableId(value = "id")
    private String id;

    private Integer killReportId;

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

    private String killTime;

    private String img;

    private Boolean isModify;

    private String state;

}
