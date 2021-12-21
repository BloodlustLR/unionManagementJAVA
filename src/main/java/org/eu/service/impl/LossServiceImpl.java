package org.eu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.Army;
import org.eu.entity.Loss;
import org.eu.mapper.LossMapper;
import org.eu.service.LossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class LossServiceImpl extends ServiceImpl<LossMapper, Loss> implements LossService{

    @Autowired
    LossMapper lossMapper;

    @Override
    public List<Loss> getPaymentArmyLoss(Integer paymentId, Integer armyId) {
        return lossMapper.getPaymentArmyLoss(paymentId, armyId);
    }

    @Override
    public Long getPaymentTotal(Integer pid) {
        QueryWrapper<Loss> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state","A")
                .eq("payment_id",pid);
        List<Loss> lossList = lossMapper.selectList(queryWrapper);
        Long total = new Long(0);
        for(Loss loss: lossList){
            if(loss.getNum()!=null){
                total+=loss.getNum();
            }
        }
        return total;
    }

    @Override
    public Map<String, Long> getPaymentAllArmyLoss(Integer pid) {
        Map<String,Long> resultMap = new HashMap<>();
        List<Loss> lossList = lossMapper.getPaymentAllArmyLoss(pid);

        for(Loss loss: lossList){
//            String name = "["+loss.getArmyShortName()+"]"+loss.getArmyName();
            String name = loss.getArmyShortName();
            if(!resultMap.containsKey(name)){
                resultMap.put(name,new Long(0));
            }
            if(loss.getNum()!=null) {
                resultMap.put(name,resultMap.get(name)+loss.getNum());
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Long> getPaymentAllTypeLoss(Integer pid) {
        Map<String,Long> resultMap = new HashMap<>();
        List<Loss> lossList = lossMapper.getPaymentAllShipLoss(pid);

        for(Loss loss: lossList){
            String name = loss.getShipType();
            if(!resultMap.containsKey(name)){
                resultMap.put(name,new Long(0));
            }
            if(loss.getNum()!=null){
                resultMap.put(name,resultMap.get(name)+loss.getNum());
            }
        }

        return resultMap;
    }

    @Override
    public Loss selectLossById(String id) {
        return lossMapper.selectLossById(id);
    }

    @Override
    public void updateLossById(Loss loss) {
        lossMapper.updateLossById(loss);
    }
}
