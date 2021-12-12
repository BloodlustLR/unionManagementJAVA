package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

    private String state;

}
