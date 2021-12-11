package org.eu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.Union;
import org.eu.mapper.UnionMapper;
import org.eu.service.UnionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnionServiceImpl extends ServiceImpl<UnionMapper, Union> implements UnionService {

    @Autowired
    UnionMapper unionMapper;

    @Override
    public List<Union> getAllUnionArmy() {
        return unionMapper.getAllUnionArmy();
    }
}
