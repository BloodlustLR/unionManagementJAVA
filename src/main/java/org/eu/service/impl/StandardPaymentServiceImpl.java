package org.eu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.StandardPayment;
import org.eu.mapper.StandardPaymentMapper;
import org.eu.service.StandardPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandardPaymentServiceImpl extends ServiceImpl<StandardPaymentMapper, StandardPayment> implements StandardPaymentService {

    @Autowired
    StandardPaymentMapper standardPaymentMapper;

    @Override
    public List<StandardPayment> getAllStandardPayment() {
        return standardPaymentMapper.getAllStandardPayment();
    }
}
