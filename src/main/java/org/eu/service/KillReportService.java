package org.eu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.eu.entity.KillReport;
import org.eu.entity.Payment;

public interface KillReportService extends IService<KillReport> {


    IPage<Payment> pageKillReport(Page<Payment> page, String startDate, String endDate);

}
