package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.eu.entity.User;
import org.eu.entity.UserRole;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {


}
