package org.eu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("user")
public class User {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    @TableField(exist=false)
    private String role;

    @TableField(exist=false)
    private Integer roleId;

    @TableField(exist=false)
    private String gameId;

    @TableField(exist=false)
    private String union;

    @TableField(exist=false)
    private Integer unionId;

    @TableField(exist=false)
    private String shortArmy;

    @TableField(exist=false)
    private String army;

    @TableField(exist=false)
    private Integer armyId;


    private String state;

}
