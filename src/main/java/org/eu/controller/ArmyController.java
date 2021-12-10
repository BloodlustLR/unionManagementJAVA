package org.eu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.eu.entity.Army;
import org.eu.service.ArmyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
