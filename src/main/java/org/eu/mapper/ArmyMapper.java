package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.eu.entity.Army;
import org.eu.entity.User;

import java.util.List;

@Mapper
public interface ArmyMapper extends BaseMapper<Army> {

    List<Army> listArmyByUnionId(Integer unionId);

    void selectArmyByGameId(String gameId);

    List<String> listArmyShorName();
}
