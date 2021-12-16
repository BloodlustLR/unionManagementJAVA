package org.eu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.eu.entity.Ship;
import org.eu.entity.User;
import org.eu.entity.UserInfo;
import org.eu.entity.UserRole;
import org.eu.service.UserInfoService;
import org.eu.service.UserRoleService;
import org.eu.service.UserService;
import org.eu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public User login(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);
        String username = strj.getString("username");
        String password = strj.getString("password");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        return userService.login(user);
    }

    @PostMapping("/register")
    public String register(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);
        String username = strj.getString("username");
        String password = strj.getString("password");
        Integer roleId = strj.getInteger("roleId")==null?4:strj.getInteger("roleId");
        String gameId = strj.getString("gameId");
        Integer armyId = strj.getInteger("armyId");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        UserInfo userInfo = new UserInfo();
        userInfo.setGameId(gameId);
        userInfo.setArmyId(armyId);

        return ResponseUtil.success(userService.register(user,userInfo,roleId));
    }

    @PostMapping("/pageUser")
    public IPage<User> pageUser(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);
        Integer unionId = strj.getInteger("union")==0?null:strj.getInteger("union");
        Integer armyId = strj.getInteger("army")==0?null:strj.getInteger("army");
        String role = strj.getString("role").equals("全部")?null:strj.getString("role");
        String gameId = strj.getString("gameId").equals("")?null:strj.getString("gameId");
        String username = strj.getString("username").equals("")?null:strj.getString("username");
        Integer pageNum = strj.getInteger("pageNum");
        Integer pageSize = strj.getInteger("pageSize");
        Page<User> page = new Page<>(pageNum,pageSize);
        User user = new User();
        user.setUnionId(unionId);
        user.setArmyId(armyId);
        user.setRole(role);
        user.setGameId(gameId);
        user.setUsername(username);
        return userService.pageUser(page,user);
    }

    @PostMapping("/configUser")
    public String configUser(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);
        Integer id = strj.getInteger("id");
        Integer armyId = strj.getInteger("armyId");
        Integer roleId = strj.getInteger("roleId");
        String gameId = strj.getString("gameId");
        String username = strj.getString("username");
        String password = strj.getString("password");


        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_id",id)
                .eq("state","A");
        UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);
        userInfo.setGameId(gameId);
        userInfo.setArmyId(armyId);
        userInfoService.updateById(userInfo);

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id",id)
                .eq("state","A");
        UserRole userRole = userRoleService.getOne(userRoleQueryWrapper);
        userRole.setRoleId(roleId);
        userRoleService.updateById(userRole);

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);

        Boolean result = userService.updateById(user);

        return ResponseUtil.success(result?"success":"fail");
    }

    @PostMapping("/removeUser")
    public String removeShip(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Integer userId = strj.getInteger("userId");

        Boolean flag = userService.removeById(userId);

        return ResponseUtil.success(flag?"success":"fail");
    }


}
