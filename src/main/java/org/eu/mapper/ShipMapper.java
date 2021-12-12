package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.eu.entity.Ship;
import org.eu.entity.shipTree.TreeType;

import java.util.List;

@Mapper
public interface ShipMapper extends BaseMapper<Ship> {

    IPage<Ship> pageShip(Page<?> page,Ship ship);


    List<TreeType> listShipTree();
}
