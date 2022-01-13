package org.eu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.eu.entity.*;
import org.eu.service.ArmyService;
import org.eu.service.KillReportService;
import org.eu.service.KillService;
import org.eu.service.ShipService;
import org.eu.util.FastjsonUtil;
import org.eu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/kill")
public class KillController {

    @Autowired
    ShipService shipService;

    @Autowired
    ArmyService armyService;

    @Autowired
    KillService killService;

    @Autowired
    KillReportService killReportService;

    @PostMapping("/addKill")
    public List<JSONObject> addKill(@RequestBody String str) throws ParseException {
        JSONArray strArr = JSONArray.parseArray(str);

        Boolean hasAchieveKillReportInfo = false;
        KillReport killReport = null;
        List<String> limitArea = null;
        List<String> limitConstellation = null;
        List<String> limitGalaxy = null;

        List<JSONObject> resultList = new ArrayList<>();
        for(int i = 0;i < strArr.size() ; i++){
            JSONObject valueStrj = strArr.getJSONObject(i);
            Integer killReportId = valueStrj.getInteger("killReportId");


            if(!hasAchieveKillReportInfo){
                killReport = killReportService.getById(killReportId);
                if(killReport.getLimitArea()!=null){
                    limitArea = FastjsonUtil.convertJSONArrayToTypeList(JSONObject.parseArray(killReport.getLimitArea()),String.class);
                }
                if(killReport.getLimitConstellation()!=null){
                    limitConstellation = FastjsonUtil.convertJSONArrayToTypeList(JSONObject.parseArray(killReport.getLimitConstellation()),String.class);
                }
                if(killReport.getLimitGalaxy()!=null){
                    limitGalaxy = FastjsonUtil.convertJSONArrayToTypeList(JSONObject.parseArray(killReport.getLimitGalaxy()),String.class);
                }
                hasAchieveKillReportInfo = true;
            }

            Integer armyId = null;
            String shortName = valueStrj.getString("kmArmyShortName");
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
                resultList.add(valueStrj);
                continue;
            }

            Ship ship = shipService.findShipByName(valueStrj.getString("shipName"));
            if(ship==null){
                valueStrj.put("info","没有船型");
                resultList.add(valueStrj);
                continue;
            }

            String area = valueStrj.getString("area")==""?null:valueStrj.getString("area");
            String constellation = valueStrj.getString("constellation")==""?null:valueStrj.getString("constellation");
            String galaxy = valueStrj.getString("galaxy")==""?null:valueStrj.getString("galaxy");
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
                valueStrj.put("info","地点不合规");
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

            Kill kill = new Kill();
            kill.setId(id);
            kill.setKillReportId(killReportId);
            kill.setArmyId(armyId);
            kill.setShipId(ship.getId());
            kill.setKillTime(valueStrj.getString("reportTime"));
            kill.setArea(area);
            kill.setConstellation(constellation);
            kill.setGalaxy(galaxy);
            kill.setNum(valueStrj.getLong("money"));
            kill.setImg(valueStrj.getString("img"));
            kill.setIsModify(valueStrj.getBoolean("isModify"));
            kill.setState("A");

            Kill oldLoss = killService.selectKillById(id);
            if(oldLoss!=null){
                killService.updateKillById(kill);
            }else{
                killService.save(kill);
            }
        }

        return resultList;
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

        String killId = strj.getString("id");

        Boolean flag = killService.removeById(killId);

        return ResponseUtil.success(flag?"success":"fail");
    }

}
