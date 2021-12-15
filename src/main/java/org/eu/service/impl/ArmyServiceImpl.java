package org.eu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.Army;
import org.eu.entity.User;
import org.eu.mapper.ArmyMapper;
import org.eu.mapper.UserMapper;
import org.eu.service.ArmyService;
import org.eu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArmyServiceImpl extends ServiceImpl<ArmyMapper, Army> implements ArmyService {

    @Autowired
    ArmyMapper armyMapper;


    @Override
    public Army getArmyByGameId(String gameId) {

        armyMapper.selectArmyByGameId(gameId);

        return null;
    }
}
