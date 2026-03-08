package com.xiangyongshe.swimadmin.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.mapper.SysUserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private final SysUserMapper sysUserMapper;

    public SecurityUtil(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    public SysUser currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return null;
        }
        return sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                .eq("username", auth.getName())
                .eq("deleted", 0));
    }
}
