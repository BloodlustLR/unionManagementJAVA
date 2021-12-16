package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.eu.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User login(User user);

    void insertUser(User user);

    IPage<User> pageUser(Page<User> page, User user);
}
