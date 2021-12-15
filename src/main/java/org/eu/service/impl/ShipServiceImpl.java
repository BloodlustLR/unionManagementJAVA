package org.eu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.Ship;
import org.eu.entity.shipTree.TreeType;
import org.eu.mapper.ShipMapper;
import org.eu.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipServiceImpl extends ServiceImpl<ShipMapper, Ship> implements ShipService {


    @Autowired
    ShipMapper shipMapper;

    @Override
    public IPage<Ship> pageShip(Page<Ship> page,Ship ship) {
        return shipMapper.pageShip(page,ship);
    }

    @Override
    public Ship findShipByName(String shipName) {
        return shipMapper.selectShipByName(shipName);
    }

    @Override
    public List<TreeType> getShipTree() {
        return shipMapper.listShipTree();
    }
}
