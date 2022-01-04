package org.eu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.eu.entity.System;
import org.eu.entity.Union;
import org.eu.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys")
public class SystemController {

    @Autowired
    SystemService systemService;

    @GetMapping("/getInfo")
    public List<System> getInfo(){
        return systemService.list();
    }

    @PostMapping("/updateInfo")
    public void updateInfo(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);
        Double rate = strj.getDouble("rate");

        QueryWrapper<System> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name","rate");
        System rateSystem = new System();
        rateSystem.setName("rate");
        rateSystem.setValue(String.valueOf(rate));
        systemService.update(rateSystem,queryWrapper);
    }

}
