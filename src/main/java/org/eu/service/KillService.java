package org.eu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.eu.entity.Kill;
import org.eu.entity.KillReport;
import org.eu.entity.Loss;
import org.eu.entity.Union;

import java.util.List;
import java.util.Map;

public interface KillService extends IService<Kill> {
    Kill selectKillById(String killId);

    void updateKillById(Kill kill);

    Long getKillTotal(KillReport killReport,List<Union> unionList);

    List<Union> getKillReportUnionArmy(KillReport killReport, List<Union> unionList);

    List<Kill> getKillReportArmyKill(KillReport killReport, List<Union> unionList, Integer armyId);

    Map<String, Long> getAllArmyRank(KillReport killReport, List<Union> unionList);

    Map<String, Long> getAllAreaKill(KillReport killReport, List<Union> unionList);

    List<String> getKillReportUnsignedArmy(KillReport killReport, List<Union> unionList);
}
