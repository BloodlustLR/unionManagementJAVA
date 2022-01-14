package org.eu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.eu.entity.KillReport;
import org.eu.entity.Payment;
import org.eu.entity.Union;
import org.eu.service.KillReportService;
import org.eu.service.KillService;
import org.eu.service.UnionService;
import org.eu.util.DateFormatUtil;
import org.eu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/killReport")
public class KillReportController {

    @Autowired
    UnionService unionService;

    @Autowired
    KillReportService killReportService;

    @Autowired
    KillService killService;

    @PostMapping("/pageKillReport")
    public IPage<Payment> pageKillReport(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        List<String> dateRange = JSONObject.parseArray(strj.getString("dateRange"),String.class);

        String startDate=null;
        String endDate=null;
        if(dateRange!=null&&dateRange.size()!=0){
            startDate = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(dateRange.get(0)));
            endDate = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(dateRange.get(1)));
        }

        Integer pageNum = strj.getInteger("pageNum");
        Integer pageSize = strj.getInteger("pageSize");
        Page<Payment> page = new Page<>(pageNum,pageSize);

        return killReportService.pageKillReport(page,startDate,endDate);
    }

    @GetMapping("/getKillReportInfo")
    public KillReport getKillReportInfo(@RequestParam("pid") Integer pid){
        return killReportService.getById(pid);
    }

    @GetMapping("/getKillReportTotal")
    public Map<String,Object> getKillReportTotal(@RequestParam("pid") Integer pid){
        Map<String,Object> resultMap = new HashMap<>();
        KillReport killReport = killReportService.getById(pid);
        List<Union> unionList = unionService.getAllUnionArmy();
        resultMap.put("killTotal",killService.getKillTotal(killReport,unionList));
        return resultMap;
    }

    @GetMapping("/getKillReportUnionArmy")
    public List<Union> getKillReportUnionArmy(@RequestParam("pid") Integer pid){
        KillReport killReport = killReportService.getById(pid);
        List<Union> unionList = unionService.getAllUnionArmy();
        return killService.getKillReportUnionArmy(killReport,unionList);
    }

    @GetMapping("/getKillReportUnsignedArmy")
    public List<String> getKillReportUnsignedArmy(@RequestParam("pid") Integer pid){
        KillReport killReport = killReportService.getById(pid);
        List<Union> unionList = unionService.getAllUnionArmy();
        return killService.getKillReportUnsignedArmy(killReport,unionList);
    }

    @PostMapping("/addKillReport")
    public String addKillReport(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        KillReport killReport = new KillReport();

        String name = strj.getString("name");
        String endTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(strj.getString("endTime")));
        Boolean needDetail = strj.getBoolean("needDetail");
        killReport.setName(name);
        killReport.setEndTime(endTime);
        killReport.setNeedDetail(needDetail);

        Boolean hasLimitTime = strj.getBoolean("hasLimitTime");
        if(hasLimitTime){
            List<String> limitTime = JSONObject.parseArray(strj.getString("limitTime"),String.class);
            String lossStartTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(limitTime.get(0)));
            String lossEndTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(limitTime.get(1)));
            killReport.setKillStartTime(lossStartTime);
            killReport.setKillEndTime(lossEndTime);
        }

        Boolean hasTargetUnion = strj.getBoolean("hasTargetUnion");
        if(hasTargetUnion){
            String targetUnion = strj.getString("targetUnion");
            killReport.setTargetUnion(targetUnion);
        }

        Boolean hasTargetArmy = strj.getBoolean("hasTargetArmy");
        if(hasTargetArmy){
            String targetArmyStr = strj.getString("targetArmy");
            killReport.setTargetArmy(targetArmyStr);
        }

        Boolean hasLimitArea = strj.getBoolean("hasLimitArea");
        if(hasLimitArea){
            String limitAreaStr = strj.getString("limitAreaList");
            killReport.setLimitArea(limitAreaStr);
        }

        Boolean hasLimitConstellation = strj.getBoolean("hasLimitConstellation");
        if(hasLimitConstellation){
            String limitConstellationStr = strj.getString("limitConstellationList");
            killReport.setLimitConstellation(limitConstellationStr);
        }

        Boolean hasLimitGalaxy = strj.getBoolean("hasLimitGalaxy");
        if(hasLimitGalaxy){
            String limitGalaxyStr = strj.getString("limitGalaxyList");
            killReport.setLimitGalaxy(limitGalaxyStr);
        }

        Boolean flag = killReportService.save(killReport);

        return ResponseUtil.success(flag?"success":"fail");
    }

    @PostMapping("/configKillReport")
    public String configKillReport(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        KillReport killReport = new KillReport();

        Integer id = strj.getInteger("id");
        String name = strj.getString("name");
        String endTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(strj.getString("endTime")));
        Boolean needDetail = strj.getBoolean("needDetail");
        killReport.setId(id);
        killReport.setName(name);
        killReport.setEndTime(endTime);
        killReport.setNeedDetail(needDetail);

        Boolean hasLimitTime = strj.getBoolean("hasLimitTime");
        if(hasLimitTime){
            List<String> limitTime = JSONObject.parseArray(strj.getString("limitTime"),String.class);
            String lossStartTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(limitTime.get(0)));
            String lossEndTime = DateFormatUtil.formatSecond(DateFormatUtil.parseDateByUTC(limitTime.get(1)));
            killReport.setKillStartTime(lossStartTime);
            killReport.setKillEndTime(lossEndTime);
        }else{
            killReport.setKillStartTime(null);
            killReport.setKillEndTime(null);
        }

        Boolean hasTargetUnion = strj.getBoolean("hasTargetUnion");
        if(hasTargetUnion){
            String targetUnion = strj.getString("targetUnion");
            killReport.setTargetUnion(targetUnion);
        }else{
            killReport.setTargetUnion(null);
        }

        Boolean hasTargetArmy = strj.getBoolean("hasTargetArmy");
        if(hasTargetArmy){
            String targetArmyStr = strj.getString("targetArmy");
            killReport.setTargetArmy(targetArmyStr);
        }else{
            killReport.setTargetArmy(null);
        }

        Boolean hasLimitArea = strj.getBoolean("hasLimitArea");
        if(hasLimitArea){
            String limitAreaStr = strj.getString("limitAreaList");
            killReport.setLimitArea(limitAreaStr);
        }else{
            killReport.setLimitArea(null);
        }

        Boolean hasLimitConstellation = strj.getBoolean("hasLimitConstellation");
        if(hasLimitConstellation){
            String limitConstellationStr = strj.getString("limitConstellationList");
            killReport.setLimitConstellation(limitConstellationStr);
        }else{
            killReport.setLimitConstellation(null);
        }

        Boolean hasLimitGalaxy = strj.getBoolean("hasLimitGalaxy");
        if(hasLimitGalaxy){
            String limitGalaxyStr = strj.getString("limitGalaxyList");
            killReport.setLimitGalaxy(limitGalaxyStr);
        }else{
            killReport.setLimitGalaxy(null);
        }

        Boolean flag =  killReportService.updateById(killReport);

        return ResponseUtil.success(flag?"success":"fail");
    }

    @PostMapping("/removeKillReport")
    public String removeKillReport(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Integer id = strj.getInteger("id");

        Boolean flag = killReportService.removeById(id);

        return ResponseUtil.success(flag?"success":"fail");
    }

}
