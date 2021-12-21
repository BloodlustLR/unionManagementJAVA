package org.eu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.Army;
import org.eu.entity.Kill;
import org.eu.entity.Loss;
import org.eu.entity.Union;
import org.eu.mapper.KillMapper;
import org.eu.service.KillService;
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
    public Long getKillTotal(Integer pid) {
        QueryWrapper<Kill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state","A")
                .eq("kill_report_id",pid);
        List<Kill> lossList = killMapper.selectList(queryWrapper);
        Long total = new Long(0);
        for(Kill kill: lossList){
            if(kill.getNum()!=null){
                total+=kill.getNum();
            }
        }
        return total;
    }

    @Override
    public List<Union> getKillReportUnionArmy(Integer pid) {
        List<Union> unionList = new ArrayList<>();

        List<Army> armyList = killMapper.getKillReportUnionArmy(pid);

        List<String> unionNameList = new ArrayList<>();
        for(Army army:armyList){
            if(!unionNameList.contains(army.getUnionName())){
                Union newUnion = new Union();
                newUnion.setName(army.getUnionName());
                newUnion.setArmyList(new ArrayList<>());
                unionList.add(newUnion);
                unionNameList.add(newUnion.getName());
            }
            army.setName("["+army.getShortName()+"]"+army.getName());
            unionList.get(unionNameList.indexOf(army.getUnionName())).getArmyList().add(army);
        }
        return unionList;
    }

    @Override
    public List<Kill> getKillReportArmyKill(Integer killReportId, Integer armyId) {
        return killMapper.getKillReportArmyKill(killReportId, armyId);
    }

    @Override
    public Map<String, Long> getAllArmyRank(Integer pid) {
        Map<String,Long> resultMap = new HashMap<>();
        List<Kill> killList = killMapper.getAllArmyRank(pid);

        for(Kill kill: killList){
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
    public Map<String, Long> getAllAreaKill(Integer pid) {

        Map<String,Long> areaMap = new HashMap<>();

        QueryWrapper<Kill> killQueryWrapper = new QueryWrapper<>();
        killQueryWrapper.eq("state","A");
        List<Kill> killList = killMapper.selectList(killQueryWrapper);

        for(Kill kill: killList){
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
}
