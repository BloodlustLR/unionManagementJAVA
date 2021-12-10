package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.eu.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User login(User user);


    void insertUser(User user);


}
