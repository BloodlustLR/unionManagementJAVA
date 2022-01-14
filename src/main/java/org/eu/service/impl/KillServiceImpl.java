package org.eu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.*;
import org.eu.mapper.KillMapper;
import org.eu.service.KillService;
import org.eu.util.FastjsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KillServiceImpl extends ServiceImpl<KillMapper, Kill> implements KillService {

    @Autowired
    KillMapper killMapper;

    @Override
    public Kill selectKillById(String killId) {
        return killMapper.selectKillById(killId);
    }

    @Override
    public void updateKillById(Kill kill) {
        killMapper.updateKillById(kill);
    }

    @Override
    public Long getKillTotal(KillReport killReport, List<Union> unionList) {

        QueryWrapper<Kill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state","A")
                .eq("kill_report_id",killReport.getId());
        List<Kill> lossList = killMapper.selectList(queryWrapper);

        Boolean limitArmy = killReport.getTargetUnion()!=null||killReport.getTargetArmy()!=null;
        List<String> allowArmy = getAllowArmy(killReport,unionList);

        Long total = new Long(0);
        for(Kill kill: lossList){

            if(limitArmy){
                if(allowArmy.indexOf(kill.getKilledArmy())==-1){
                    continue;
                }
            }

            if(kill.getNum()!=null){
                total+=kill.getNum();
            }
        }
        return total;
    }

    @Override
    public List<Union> getKillReportUnionArmy(KillReport killReport, List<Union> unionList) {
        List<Union> finalUnionList = new ArrayList<>();

        List<Kill> killList = killMapper.getKillReportUnionArmy(killReport.getId());

        Boolean limitArmy = killReport.getTargetUnion()!=null||killReport.getTargetArmy()!=null;
        List<String> allowArmy = getAllowArmy(killReport,unionList);

        List<String> unionNameList = new ArrayList<>();
        for(Kill kill:killList){

            if(limitArmy){
                if(allowArmy.indexOf(kill.getKilledArmy())==-1){
                    continue;
                }
            }

            if(!unionNameList.contains(kill.getUnionName())){
                Union newUnion = new Union();
                newUnion.setName(kill.getUnionName());
                newUnion.setArmyList(new ArrayList<>());
                finalUnionList.add(newUnion);
                unionNameList.add(newUnion.getName());
            }
            String armyName = ("["+kill.getArmyShortName()+"]"+kill.getArmyName());
            Union union= finalUnionList.get(unionNameList.indexOf(kill.getUnionName()));
            List<Army> armyList = union.getArmyList();

            if(armyList.indexOf(armyName)==-1){
                Army army = new Army();
                army.setId(kill.getArmyId());
                army.setName(armyName);
                armyList.add(army);
            }

        }
        return finalUnionList;
    }

    @Override
    public List<Kill> getKillReportArmyKill(KillReport killReport,List<Union> unionList, Integer armyId) {

        List<Kill> killList = killMapper.getKillReportArmyKill(killReport.getId(), armyId);
        List<Kill> resultList = new ArrayList<>();

        Boolean limitArmy = killReport.getTargetUnion()!=null||killReport.getTargetArmy()!=null;
        List<String> allowArmy = getAllowArmy(killReport,unionList);

        for(Kill kill:killList){
            if(limitArmy){
                if(allowArmy.indexOf(kill.getKilledArmy())==-1){
                    continue;
                }
            }
            resultList.add(kill);
        }

        return resultList;
    }

    @Override
    public Map<String, Long> getAllArmyRank(KillReport killReport, List<Union> unionList) {
        Map<String,Long> resultMap = new HashMap<>();
        List<Kill> killList = killMapper.getAllArmyRank(killReport.getId());

        Boolean limitArmy = killReport.getTargetUnion()!=null||killReport.getTargetArmy()!=null;
        List<String> allowArmy = getAllowArmy(killReport,unionList);

        for(Kill kill: killList){

            if(limitArmy){
                if(allowArmy.indexOf(kill.getKilledArmy())==-1){
                    continue;
                }
            }

//            String name = "["+kill.getArmyShortName()+"]"+kill.getArmyName();
            String name = kill.getArmyShortName();
            if(!resultMap.containsKey(name)){
                resultMap.put(name,new Long(0));
            }
            if(kill.getNum()!=null) {
                resultMap.put(name,resultMap.get(name)+kill.getNum());
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Long> getAllAreaKill(KillReport killReport, List<Union> unionList) {

        Map<String,Long> areaMap = new HashMap<>();

        QueryWrapper<Kill> killQueryWrapper = new QueryWrapper<>();
        killQueryWrapper.eq("kill_report_id",killReport.getId()).eq("state","A");
        List<Kill> killList = killMapper.selectList(killQueryWrapper);

        Boolean limitArmy = killReport.getTargetUnion()!=null||killReport.getTargetArmy()!=null;
        List<String> allowArmy = getAllowArmy(killReport,unionList);

        for(Kill kill: killList){

            if(limitArmy){
                if(allowArmy.indexOf(kill.getKilledArmy())==-1){
                    continue;
                }
            }

            String name = kill.getArea();
            if(!areaMap.containsKey(name)){
                areaMap.put(name,new Long(0));
            }
            if(kill.getNum()!=null) {
                areaMap.put(name,areaMap.get(name)+kill.getNum());
            }
        }

        return areaMap;
    }

    @Override
    public List<String> getKillReportUnsignedArmy(KillReport killReport, List<Union> unionList) {

        QueryWrapper<Kill> killQueryWrapper = new QueryWrapper<>();
        killQueryWrapper.eq("kill_report_id",killReport.getId()).eq("state","A");
        List<Kill> killList = killMapper.selectList(killQueryWrapper);

        Boolean limitArmy = killReport.getTargetUnion()!=null||killReport.getTargetArmy()!=null;
        List<String> allowArmy = getAllowArmy(killReport,unionList);

        List<String> unsignedArmyList = new ArrayList<>();

        for(Kill kill: killList){

            if(limitArmy){
                if(allowArmy.indexOf(kill.getKilledArmy())!=-1){
                    continue;
                }
            }

            if(unsignedArmyList.indexOf(kill.getKilledArmy())==-1){
                unsignedArmyList.add(kill.getKilledArmy());
            }

        }
        return unsignedArmyList;
    }

    public List<String> getAllowArmy(KillReport killReport,List<Union> unionList){
        List<String> allowArmy = new ArrayList<>();
        if(killReport.getTargetUnion()!=null){
            List<String> targetUnion = FastjsonUtil.convertJSONArrayToTypeList(JSONObject.parseArray(killReport.getTargetUnion()),String.class);
            for(String unionShortName : targetUnion){
                for(Union union : unionList){
                    if(union.getShortName().equals(unionShortName)){
                        for(Army army:union.getArmyList()){
                            if(allowArmy.indexOf(army.getShortName())==-1){
                                allowArmy.add(army.getShortName());
                            }
                        }
                        break;
                    }
                }
            }
        }

        if(killReport.getTargetArmy()!=null){
            List<String> targetArmy = FastjsonUtil.convertJSONArrayToTypeList(JSONObject.parseArray(killReport.getTargetArmy()),String.class);
            for(String armyShortName : targetArmy){
                if(allowArmy.indexOf(armyShortName)==-1){
                    allowArmy.add(armyShortName);
                }
            }
        }

        return allowArmy;
    }

}
