package org.eu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.KillReport;
import org.eu.entity.Payment;
import org.eu.entity.PaymentStandardPayment;
import org.eu.mapper.KillReportMapper;
import org.eu.service.KillReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KillReportServiceImpl extends ServiceImpl<KillReportMapper, KillReport> implements KillReportService {

    @Autowired
    KillReportMapper killReportMapper;

    @Override
    public IPage<Payment> pageKillReport(Page<Payment> page, String startDate, String endDate) {
        return killReportMapper.pageKillReport(page,startDate,endDate);
    }

}
