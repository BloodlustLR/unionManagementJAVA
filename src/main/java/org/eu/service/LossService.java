package org.eu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.eu.entity.Loss;

import java.util.List;
import java.util.Map;

public interface LossService extends IService<Loss> {


    List<Loss> getPaymentArmyLoss(Integer paymentId, Integer armyId);

    Long getPaymentTotal(Integer pid);

    Map<String, Long> getPaymentAllArmyLoss(Integer pid);

    Map<String, Long> getPaymentAllTypeLoss(Integer pid);

    Map<String, Long> getPaymentAllArmy(Integer pid);

    Map<String, Long> getPaymentAllType(Integer pid);

    Loss selectLossById(String id);

    void updateLossById(Loss loss);


}
