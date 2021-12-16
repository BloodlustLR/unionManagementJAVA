package org.eu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.User;
import org.eu.entity.UserInfo;

public interface UserService extends IService<User> {

    User login(User user);

    String register(User user, UserInfo userInfo, Integer roleId);

    IPage<User> pageUser(Page<User> page, User user);
}
