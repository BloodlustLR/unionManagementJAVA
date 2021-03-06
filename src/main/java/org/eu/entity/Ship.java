package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("ship")
@Data
public class Ship {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;

    private String type;

    private Integer level;

    @TableField(exist=false)
    private Long price;

    private String state;

}
