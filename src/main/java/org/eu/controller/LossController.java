package org.eu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.eu.entity.Army;
import org.eu.entity.Loss;
import org.eu.entity.Ship;
import org.eu.mapper.ShipMapper;
import org.eu.service.ArmyService;
import org.eu.service.LossService;
import org.eu.service.PaymentService;
import org.eu.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loss")
public class LossController {

    @Autowired
    LossService lossService;

    @Autowired
    ArmyService armyService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    ShipService shipService;

    @PostMapping("addLoss")
    public Map<String,Object> addLoss(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Map<String,Object> resultMap = new HashMap<>();

        Boolean hasAchieveShipList = false;
        List<Ship> shipList = new ArrayList<>();

        for(String key:strj.keySet()){

            System.out.println(key);

            JSONObject valueStrj = strj.getJSONObject(key);
            Integer paymentId = valueStrj.getInteger("paymentId");

            if(!hasAchieveShipList){
                shipList = paymentService.listPaymentShip(paymentId);
            }

            Integer armyId = null;
            String shortName = valueStrj.getString("armyShortName");
            if(shortName!=null){
                QueryWrapper<Army> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("short_name",valueStrj.getString("armyShortName"))
                        .eq("state",'A');
                Army army = armyService.getOne(queryWrapper);
                armyId = army==null?null:army.getId();
            }
            if(armyId==null){
                Army army = armyService.getArmyByGameId(valueStrj.getString("gameId"));
                armyId = army==null?null:army.getId();
            }

            if(armyId==null){
                valueStrj.put("info","没有军团信息");
                resultMap.put(key,valueStrj);
                continue;
            }

            Ship ship = shipService.findShipByName(valueStrj.getString("shipName"));
            if(ship==null){
                valueStrj.put("info","没有船型");
                resultMap.put(key,valueStrj);
                continue;
            }

            Boolean findFlag = false;
            for(Ship allowShip: shipList){
                if(allowShip.getName().equals(ship.getName())){
                    findFlag = true;
                    break;
                }
            }
            if(!findFlag){
                valueStrj.put("info","船型不合规");
                resultMap.put(key,valueStrj);
                continue;
            }

            Loss loss = new Loss();
            loss.setId(key);
            loss.setPaymentId(paymentId);
            loss.setArmyId(armyId);
            loss.setShipId(ship.getId());
            loss.setLossTime(valueStrj.getString("reportTime"));
            loss.setArea(valueStrj.getString("area"));
            loss.setConstellation(valueStrj.getString("constellation"));
            loss.setGalaxy(valueStrj.getString("galaxy"));
            loss.setNum(valueStrj.getLong("money"));
            loss.setKmShip("kmShip");
            loss.setHighAtkShip("highATKShip");
            loss.setImg(valueStrj.getString("img"));
            lossService.saveOrUpdate(loss);
        }

        return resultMap;
    }

}
