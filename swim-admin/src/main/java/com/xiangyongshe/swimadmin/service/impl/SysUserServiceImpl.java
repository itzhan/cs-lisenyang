package com.xiangyongshe.swimadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangyongshe.swimadmin.entity.SysRole;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.entity.SysUserRole;
import com.xiangyongshe.swimadmin.exception.BizException;
import com.xiangyongshe.swimadmin.mapper.SysRoleMapper;
import com.xiangyongshe.swimadmin.mapper.SysUserMapper;
import com.xiangyongshe.swimadmin.mapper.SysUserRoleMapper;
import com.xiangyongshe.swimadmin.service.SysUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final PasswordEncoder passwordEncoder;

    public SysUserServiceImpl(SysRoleMapper sysRoleMapper,
                              SysUserRoleMapper sysUserRoleMapper,
                              PasswordEncoder passwordEncoder) {
        this.sysRoleMapper = sysRoleMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SysUser createUserWithRole(SysUser user, String roleKey) {
        SysUser exists = this.baseMapper.selectOne(new QueryWrapper<SysUser>()
                .eq("username", user.getUsername()));
        if (exists != null) {
            throw new BizException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.baseMapper.insert(user);
        SysRole role = sysRoleMapper.selectOne(new QueryWrapper<SysRole>()
                .eq("role_key", roleKey));
        if (role == null) {
            throw new BizException("角色不存在");
        }
        SysUserRole rel = new SysUserRole();
        rel.setUserId(user.getId());
        rel.setRoleId(role.getId());
        sysUserRoleMapper.insert(rel);
        return user;
    }
}
