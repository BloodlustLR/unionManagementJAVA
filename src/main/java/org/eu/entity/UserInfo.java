package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_info")
public class UserInfo {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String gameId;

    private Integer armyId;

    private String state;

    @TableField(exist=false)
    private String union;

    @TableField(exist=false)
    private String army;

}
