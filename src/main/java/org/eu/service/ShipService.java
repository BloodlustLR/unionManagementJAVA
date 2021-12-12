package org.eu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.eu.entity.Ship;
import org.eu.entity.shipTree.TreeType;

import java.util.List;

public interface ShipService extends IService<Ship> {

    IPage<Ship> pageShip(Page<Ship> page,Ship ship);

    List<TreeType> getShipTree();
}
