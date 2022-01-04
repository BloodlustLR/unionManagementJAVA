package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("`system`")
public class System {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;

    private String value;
}
