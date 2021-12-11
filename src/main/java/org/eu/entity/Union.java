package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("`union`")
public class Union {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;

    @TableField(exist=false)
    private List<Army> armyList;

    private String state;

}
