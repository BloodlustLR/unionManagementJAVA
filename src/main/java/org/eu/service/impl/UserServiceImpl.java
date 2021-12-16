package org.eu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.User;
import org.eu.entity.UserInfo;
import org.eu.entity.UserRole;
import org.eu.mapper.UserInfoMapper;
import org.eu.mapper.UserMapper;
import org.eu.mapper.UserRoleMapper;
import org.eu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public User login(User user) {
        User selectUser = userMapper.login(user);
        return selectUser;
    }

    @Override
    public String register(User user, UserInfo userInfo,Integer roleId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",user.getUsername())
                .eq("state","A");
        User selectUser = userMapper.selectOne(wrapper);
        if(selectUser!=null){
            return "usernameExist";
        }

        QueryWrapper<UserInfo> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("game_id",userInfo.getGameId())
                .eq("state","A");
        UserInfo selectUserInfo = userInfoMapper.selectOne(wrapper2);
        if(selectUserInfo!=null){
            return "gameIdExist";
        }

        userMapper.insertUser(user);
        userInfo.setUserId(user.getId());
        userInfoMapper.insert(userInfo);

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);

        return "success";
    }

    @Override
    public IPage<User> pageUser(Page<User> page, User user) {
        return userMapper.pageUser(page,user);
    }
}
