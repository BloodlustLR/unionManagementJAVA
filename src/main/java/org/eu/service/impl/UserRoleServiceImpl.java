package org.eu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.entity.UserRole;
import org.eu.mapper.UserRoleMapper;
import org.eu.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
