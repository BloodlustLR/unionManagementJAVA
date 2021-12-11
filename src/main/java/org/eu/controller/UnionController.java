package org.eu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.eu.entity.Army;
import org.eu.entity.Union;
import org.eu.service.ArmyService;
import org.eu.service.UnionService;
import org.eu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/union")
public class UnionController {

    @Autowired
    UnionService unionService;

    @Autowired
    ArmyService armyService;

    @GetMapping("/getAllUnionArmy")
    public List<Union> getAllUnionArmy(){
        return unionService.getAllUnionArmy();
    }

    @PostMapping("/addUnion")
    public String addUnion(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Union union = new Union();
        union.setName(strj.getString("name"));

        Boolean result = unionService.save(union);

        return ResponseUtil.success(result?"success":"fail");
    }

    @PostMapping("/removeUnion")
    public String removeUnion(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Integer unionId = strj.getInteger("unionId");

        QueryWrapper<Army> wrapper = new QueryWrapper<Army>();
        armyService.remove(wrapper.eq("union_id",unionId));
        Boolean result = unionService.removeById(unionId);

        return ResponseUtil.success(result?"success":"fail");
    }


}
