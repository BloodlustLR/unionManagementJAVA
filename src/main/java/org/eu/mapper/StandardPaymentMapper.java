package org.eu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.eu.entity.StandardPayment;

import java.util.List;

@Mapper
public interface StandardPaymentMapper extends BaseMapper<StandardPayment> {
    List<StandardPayment> getAllStandardPayment();
}
