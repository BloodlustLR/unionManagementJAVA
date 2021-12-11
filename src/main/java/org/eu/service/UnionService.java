package org.eu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.eu.entity.Union;
import org.eu.entity.User;

import java.util.List;

public interface UnionService extends IService<Union> {
    List<Union> getAllUnionArmy();
}
