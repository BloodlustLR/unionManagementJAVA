package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.eu.entity.Union;

import java.util.List;

@Mapper
public interface UnionMapper extends BaseMapper<Union> {

    List<Union> getAllUnionArmy();

}
