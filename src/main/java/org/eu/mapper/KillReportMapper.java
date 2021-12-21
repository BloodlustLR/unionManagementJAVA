package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.eu.entity.KillReport;
import org.eu.entity.Payment;

@Mapper
public interface KillReportMapper extends BaseMapper<KillReport> {
    IPage<Payment> pageKillReport(Page<Payment> page, String startDate, String endDate);

}
