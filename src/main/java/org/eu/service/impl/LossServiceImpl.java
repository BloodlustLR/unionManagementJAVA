package org.eu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.Loss;
import org.eu.mapper.LossMapper;
import org.eu.service.LossService;
import org.springframework.stereotype.Service;


@Service
public class LossServiceImpl extends ServiceImpl<LossMapper, Loss> implements LossService{
}
