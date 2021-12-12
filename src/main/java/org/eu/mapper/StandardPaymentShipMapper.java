package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.eu.entity.Ship;
import org.eu.entity.StandardPaymentShip;

import java.util.List;

@Mapper
public interface StandardPaymentShipMapper extends BaseMapper<StandardPaymentShip> {

    List<Ship> listRelationShip(Integer id);

}
