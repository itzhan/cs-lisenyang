package com.xiangyongshe.swimadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiangyongshe.swimadmin.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    SysUser createUserWithRole(SysUser user, String roleKey);
}
