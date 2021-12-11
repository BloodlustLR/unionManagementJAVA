package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("army")
public class Army {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;

    private String shortName;

    private Integer unionId;

    private String state;

}
