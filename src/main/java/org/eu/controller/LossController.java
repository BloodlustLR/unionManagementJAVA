package org.eu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.eu.entity.Army;
import org.eu.entity.Loss;
import org.eu.entity.Payment;
import org.eu.entity.Ship;
import org.eu.mapper.ShipMapper;
import org.eu.service.ArmyService;
import org.eu.service.LossService;
import org.eu.service.PaymentService;
import org.eu.service.ShipService;
import org.eu.util.FastjsonUtil;
import org.eu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @PostMapping("/addLoss")
    public List<JSONObject> addLoss(@RequestBody String str) throws ParseException {
        JSONArray strArr = JSONArray.parseArray(str);

        List<JSONObject> resultList = new ArrayList<>();

        Boolean hasAchievePaymentInfo = false;
        List<Ship> shipList = new ArrayList<>();
        Payment payment = null;
        List<String> limitArea = null;
        List<String> limitConstellation = null;
        List<String> limitGalaxy = null;


        for(int i = 0;i < strArr.size() ; i++) {
            JSONObject valueStrj = strArr.getJSONObject(i);
            Integer paymentId = valueStrj.getInteger("paymentId");

            if(!hasAchievePaymentInfo){
                shipList = paymentService.listPaymentShip(paymentId);
                payment = paymentService.getById(paymentId);
                if(payment.getLimitArea()!=null){
                    limitArea = FastjsonUtil.convertJSONArrayToTypeList(JSONObject.parseArray(payment.getLimitArea()),String.class);
                }
                if(payment.getLimitConstellation()!=null){
                    limitConstellation = FastjsonUtil.convertJSONArrayToTypeList(JSONObject.parseArray(payment.getLimitConstellation()),String.class);
                }
                if(payment.getLimitGalaxy()!=null){
                    limitGalaxy = FastjsonUtil.convertJSONArrayToTypeList(JSONObject.parseArray(payment.getLimitGalaxy()),String.class);
                }
                hasAchievePaymentInfo = true;
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
                valueStrj.put("info","??????????????????????????????????????????");
                resultList.add(valueStrj);
                continue;
            }

            Ship ship = shipService.findShipByName(valueStrj.getString("shipName"));
            if(ship==null){
                valueStrj.put("info","????????????");
                resultList.add(valueStrj);
                continue;
            }

            Ship findShip = null;
            for(Ship allowShip: shipList){
                if(allowShip.getName().equals(ship.getName())){
                    findShip = allowShip;
                    break;
                }
            }
            if(findShip==null){
                valueStrj.put("info","???????????????");
                resultList.add(valueStrj);
                continue;
            }

            String area = valueStrj.getString("area").equals("")?null:valueStrj.getString("area");
            String constellation = valueStrj.getString("constellation").equals("")?null:valueStrj.getString("constellation");
            String galaxy = valueStrj.getString("galaxy").equals("")?null:valueStrj.getString("galaxy");
            Boolean isInclude = false;
            if(limitArea!=null||limitConstellation!=null||limitGalaxy!=null){
                if(limitArea!=null&&area!=null){
                    if(limitArea.indexOf(area)!=-1){
                        isInclude = true;
                    }
                }
                if(limitConstellation!=null&&constellation!=null){
                    if(limitConstellation.indexOf(constellation)!=-1){
                        isInclude = true;
                    }
                }
                if(limitGalaxy!=null&&galaxy!=null){
                    if(limitGalaxy.indexOf(galaxy)!=-1){
                        isInclude = true;
                    }
                }
            }else{
                isInclude = true;
            }

            if(!isInclude){
                valueStrj.put("info","???????????????");
                resultList.add(valueStrj);
            }

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            cal.setTime(sdf.parse(valueStrj.getString("reportTime")));
            StringBuilder idBuilder = new StringBuilder();
            idBuilder.append(armyId);
            idBuilder.append("-");
            idBuilder.append(cal.getTimeInMillis());
            idBuilder.append("-");
            idBuilder.append(valueStrj.getLong("money"));
            String id = idBuilder.toString();

            Loss loss = new Loss();
            loss.setId(id);
            loss.setPaymentId(paymentId);
            loss.setArmyId(armyId);
            loss.setShipId(ship.getId());
            loss.setLossTime(valueStrj.getString("reportTime"));
            loss.setArea(area);
            loss.setConstellation(constellation);
            loss.setGalaxy(galaxy);
            loss.setNum(valueStrj.getLong("money"));
            loss.setKmShip(valueStrj.getString("kmShip"));
            loss.setHighAtkShip(valueStrj.getString("highATKShip"));
            loss.setImg(valueStrj.getString("img"));
            loss.setPrice(findShip.getPrice());
            loss.setIsModify(valueStrj.getBoolean("isModify"));
            loss.setState("A");

            Loss oldLoss = lossService.selectLossById(id);
            if(oldLoss!=null){
                lossService.updateLossById(loss);
            }else{
                lossService.save(loss);
            }

        }
        return resultList;
    }

    @PostMapping("/updateLoss")
    public String updateLoss(@RequestBody String str){
        Boolean hasAchieveShipList = false;
        List<Ship> shipList = new ArrayList<>();

        JSONObject valueStrj = JSONObject.parseObject(str);
        Integer paymentId = valueStrj.getInteger("paymentId");

        if(!hasAchieveShipList){
            shipList = paymentService.listPaymentShip(paymentId);
            hasAchieveShipList = true;
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
            return ResponseUtil.success("??????????????????");
        }

        Ship ship = shipService.findShipByName(valueStrj.getString("shipName"));
        if(ship==null){
            return ResponseUtil.success("????????????");
        }

        Ship findShip = null;
        for(Ship allowShip: shipList){
            if(allowShip.getName().equals(ship.getName())){
                findShip = allowShip;
                break;
            }
        }
        if(findShip==null){
            return ResponseUtil.success("???????????????");
        }

        Loss loss = new Loss();
        loss.setId(valueStrj.getString("reportId"));
        loss.setPaymentId(paymentId);
        loss.setArmyId(armyId);
        loss.setShipId(ship.getId());
        loss.setLossTime(valueStrj.getString("reportTime"));
        loss.setArea(valueStrj.getString("area"));
        loss.setConstellation(valueStrj.getString("constellation"));
        loss.setGalaxy(valueStrj.getString("galaxy"));
        loss.setNum(valueStrj.getLong("money"));
        loss.setPrice(findShip.getPrice());
        loss.setKmShip(valueStrj.getString("kmShip"));
        loss.setHighAtkShip(valueStrj.getString("highATKShip"));
        loss.setImg(valueStrj.getString("img"));
        loss.setIsModify(valueStrj.getBoolean("isModify"));
        loss.setState("A");

        lossService.updateLossById(loss);
        return ResponseUtil.success("success");
    }


    @PostMapping("/getPaymentArmyLoss")
    public List<Loss> getPaymentArmyLoss(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Integer armyId = strj.getInteger("armyId");
        Integer paymentId = strj.getInteger("paymentId");
        return lossService.getPaymentArmyLoss(paymentId,armyId);
    }

    @GetMapping("/getPaymentAllArmyLoss")
    public Map<String,Long> getPaymentAllArmyLoss(@RequestParam("pid") Integer pid){
        return lossService.getPaymentAllArmyLoss(pid);
    }

    @GetMapping("/getPaymentAllTypeLoss")
    public Map<String,Long> getPaymentAllTypeLoss(@RequestParam("pid") Integer pid){
        return lossService.getPaymentAllTypeLoss(pid);
    }

    @GetMapping("/getPaymentAllArmy")
    public Map<String,Long> getPaymentAllArmy(@RequestParam("pid") Integer pid){
        return lossService.getPaymentAllArmy(pid);
    }

    @GetMapping("/getPaymentAllType")
    public Map<String,Long> getPaymentAllType(@RequestParam("pid") Integer pid){
        return lossService.getPaymentAllType(pid);
    }

    @PostMapping("/removeLoss")
    public String removeLoss(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        String lossId = strj.getString("id");

        Boolean flag = lossService.removeById(lossId);

        return ResponseUtil.success(flag?"success":"fail");
    }

}
