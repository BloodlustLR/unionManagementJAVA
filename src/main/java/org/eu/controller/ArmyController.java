package org.eu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.eu.entity.Army;
import org.eu.service.ArmyService;
import org.eu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/army")
public class ArmyController {

    @Autowired
    ArmyService armyService;


    @GetMapping("/getAllArmy")
    public List<Army> getAllArmy(){
        List<Army> armyList = new ArrayList<>();
        QueryWrapper<Army> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state","A");
        armyList = armyService.list(queryWrapper);
        return armyList;
    }

    @PostMapping("/addArmy")
    public String addArmy(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Army army = new Army();
        army.setName(strj.getString("name"));
        army.setShortName(strj.getString("shortName"));
        army.setUnionId(strj.getIntValue("unionId"));

        Boolean result = armyService.save(army);

        return ResponseUtil.success(result?"success":"fail");
    }

    @PostMapping("/removeArmy")
    public String removeArmy(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Integer armyId = strj.getInteger("armyId");

        Boolean flag = armyService.removeById(armyId);

        return ResponseUtil.success(flag?"success":"fail");
    }

}
