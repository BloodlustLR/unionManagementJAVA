package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.eu.entity.Army;
import org.eu.entity.Kill;
import org.eu.entity.Loss;

import java.util.List;

@Mapper
public interface KillMapper extends BaseMapper<Kill> {
    Kill selectKillById(String killId);

    void updateKillById(Kill kill);

    List<Army> getKillReportUnionArmy(Integer pid);

    List<Kill> getKillReportArmyKill(Integer killReportId, Integer armyId);

    List<Kill> getAllArmyRank(Integer pid);
}
