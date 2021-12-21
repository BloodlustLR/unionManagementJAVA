package org.eu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.eu.entity.Kill;
import org.eu.entity.Loss;
import org.eu.entity.Union;

import java.util.List;
import java.util.Map;

public interface KillService extends IService<Kill> {
    Kill selectKillById(String killId);

    void updateKillById(Kill kill);

    Long getKillTotal(Integer pid);

    List<Union> getKillReportUnionArmy(Integer pid);

    List<Kill> getKillReportArmyKill(Integer killReportId, Integer armyId);

    Map<String, Long> getAllArmyRank(Integer pid);

    Map<String, Long> getAllAreaKill(Integer pid);
}
