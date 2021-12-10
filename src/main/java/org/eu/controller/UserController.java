package org.eu.controller;

import com.alibaba.fastjson.JSONObject;
import org.eu.entity.User;
import org.eu.entity.UserInfo;
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

    @PostMapping("register")
    public String register(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);
        String username = strj.getString("username");
        String password = strj.getString("password");
        String gameId = strj.getString("gameId");
        Integer armyId = strj.getInteger("armyId");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        UserInfo userInfo = new UserInfo();
        userInfo.setGameId(gameId);
        userInfo.setArmyId(armyId);

        return ResponseUtil.success(userService.register(user,userInfo));
    }


}
