package org.eu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.eu.entity.Army;
import org.eu.entity.Kill;
import org.eu.entity.Loss;
import org.eu.entity.Ship;
import org.eu.service.ArmyService;
import org.eu.service.KillService;
import org.eu.service.ShipService;
import org.eu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kill")
public class KillController {

    @Autowired
    ShipService shipService;

    @Autowired
    ArmyService armyService;

    @Autowired
    KillService killService;

    @PostMapping("/addKill")
    public Map<String,Object> addLoss(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Map<String,Object> resultMap = new HashMap<>();


        for(String key:strj.keySet()){

            System.out.println(key);

            JSONObject valueStrj = strj.getJSONObject(key);
            Integer killReportId = valueStrj.getInteger("killReportId");

            Integer armyId = null;
            String shortName = valueStrj.getString("armyShortName");
            if(shortName!=null){
                QueryWrapper<Army> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("short_name",valueStrj.getString("kmArmyShortName"))
                        .eq("state",'A');
                Army army = armyService.getOne(queryWrapper);
                armyId = army==null?null:army.getId();
            }
            if(armyId==null){
                Army army = armyService.getArmyByGameId(valueStrj.getString("kmGameId"));
                armyId = army==null?null:army.getId();
            }

            if(armyId==null){
                valueStrj.put("info","军团未入库，请联系管理员添加");
                resultMap.put(key,valueStrj);
                continue;
            }

            Ship ship = shipService.findShipByName(valueStrj.getString("shipName"));
            if(ship==null){
                valueStrj.put("info","没有船型");
                resultMap.put(key,valueStrj);
                continue;
            }

            Kill kill = new Kill();
            kill.setId(key);
            kill.setKillReportId(killReportId);
            kill.setArmyId(armyId);
            kill.setShipId(ship.getId());
            kill.setKillTime(valueStrj.getString("reportTime"));
            kill.setArea(valueStrj.getString("area"));
            kill.setConstellation(valueStrj.getString("constellation"));
            kill.setGalaxy(valueStrj.getString("galaxy"));
            kill.setNum(valueStrj.getLong("money"));
            kill.setImg(valueStrj.getString("img"));
            kill.setIsModify(valueStrj.getBoolean("isModify"));
            kill.setState("A");

            Kill oldLoss = killService.selectKillById(key);
            System.out.println(oldLoss);
            if(oldLoss!=null){
                killService.updateKillById(kill);
            }else{
                killService.save(kill);
            }
        }
        return resultMap;
    }

    @PostMapping("/getKillReportArmyKill")
    public List<Kill> getKillReportArmyKill(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Integer armyId = strj.getInteger("armyId");
        Integer killReportId = strj.getInteger("killReportId");
        return killService.getKillReportArmyKill(killReportId,armyId);
    }

    @GetMapping("/getAllArmyRank")
    public Map<String,Long> getAllArmyRank(@RequestParam("pid") Integer pid){
        return killService.getAllArmyRank(pid);
    }

    @GetMapping("/getAllAreaKill")
    public Map<String,Long> getAllAreaKill(@RequestParam("pid") Integer pid){
        return killService.getAllAreaKill(pid);
    }


    @PostMapping("/removeKill")
    public String removeLoss(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Integer lossId = strj.getInteger("id");

        Boolean flag = killService.removeById(lossId);

        return ResponseUtil.success(flag?"success":"fail");
    }

}
