package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.eu.entity.Army;
import org.eu.entity.User;

@Mapper
public interface ArmyMapper extends BaseMapper<Army> {
}
