package org.eu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.eu.entity.Ship;
import org.eu.entity.StandardPaymentShip;
import org.eu.entity.shipTree.TreeLevel;
import org.eu.entity.shipTree.TreeType;
import org.eu.service.ShipService;
import org.eu.service.StandardPaymentShipService;
import org.eu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ship")
public class ShipController {

    @Autowired
    ShipService shipService;

    @Autowired
    StandardPaymentShipService standardPaymentShipService;

    @PostMapping("/pageShip")
    public IPage<Ship> pageShip(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);
        String type = strj.getString("type").equals("全部")?null:strj.getString("type");
        Integer level = strj.getInteger("level")==0?null:strj.getInteger("level");
        Integer pageNum = strj.getInteger("pageNum");
        Integer pageSize = strj.getInteger("pageSize");
        Page<Ship> page = new Page<>(pageNum,pageSize);
        Ship ship = new Ship();
        ship.setLevel(level);
        ship.setType(type);
        return shipService.pageShip(page,ship);
    }

    @GetMapping("/getAllShip")
    public List<Ship> getAllShip(){
        List<Ship> shipList = new ArrayList<>();
        QueryWrapper<Ship> shipQueryWrapper = new QueryWrapper<>();
        shipQueryWrapper.eq("state","A");
        shipList = shipService.list(shipQueryWrapper);
        return shipList;
    }

    @GetMapping("/getShipTree")
    public List<TreeType> getShipTree(){
        List<TreeType> treeList = shipService.getShipTree();

        for(TreeType treeType:treeList){
            treeType.setId(treeType.getLabel());
            for(TreeLevel treeLevel :treeType.getChildren()){
                treeLevel.setLabel("T"+treeLevel.getLabel());
                treeLevel.setId(treeType.getLabel()+"_"+treeLevel.getLabel());
            }
        }
        return treeList;
    }

    @PostMapping("/addShip")
    public String addShip(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Ship ship = new Ship();
        ship.setType(strj.getString("type"));
        ship.setLevel(strj.getInteger("level"));
        ship.setName(strj.getString("name"));

        Boolean result = shipService.save(ship);

        return ResponseUtil.success(result?"success":"fail");
    }

    @PostMapping("/configShip")
    public String configShip(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Ship ship = new Ship();
        ship.setId(strj.getInteger("id"));
        ship.setType(strj.getString("type"));
        ship.setLevel(strj.getInteger("level"));
        ship.setName(strj.getString("name"));

        Boolean result = shipService.updateById(ship);

        return ResponseUtil.success(result?"success":"fail");
    }

    @PostMapping("/removeShip")
    public String removeShip(@RequestBody String str){
        JSONObject strj = JSONObject.parseObject(str);

        Integer shipId = strj.getInteger("shipId");

//        QueryWrapper<StandardPaymentShip> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("ship_id",shipId);
//        standardPaymentShipService.remove(queryWrapper);

        Boolean flag = shipService.removeById(shipId);

        return ResponseUtil.success(flag?"success":"fail");
    }

}
