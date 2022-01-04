package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.eu.entity.Army;
import org.eu.entity.Loss;

import java.util.List;
import java.util.Map;

@Mapper
public interface LossMapper extends BaseMapper<Loss> {

    List<Loss> getPaymentLossWithUnionArmy(@Param("paymentId") Integer paymentId);

    List<Loss> getPaymentArmyLoss(@Param("paymentId") Integer paymentId, @Param("armyId") Integer armyId);

    List<Loss> getPaymentAllArmyLoss(Integer pid);

    List<Loss> getPaymentAllShipLoss(Integer pid);

    Loss selectLossById(String id);

    void updateLossById(Loss loss);
}
