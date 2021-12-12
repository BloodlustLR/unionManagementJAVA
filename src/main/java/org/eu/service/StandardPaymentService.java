package org.eu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.eu.entity.StandardPayment;

import java.util.List;

public interface StandardPaymentService extends IService<StandardPayment> {
    List<StandardPayment> getAllStandardPayment();
}
