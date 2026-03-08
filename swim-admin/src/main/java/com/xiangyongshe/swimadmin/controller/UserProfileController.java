package com.xiangyongshe.swimadmin.controller;

import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.service.SysUserService;
import com.xiangyongshe.swimadmin.util.SecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/profile")
public class UserProfileController {

    private final SecurityUtil securityUtil;
    private final SysUserService sysUserService;

    public UserProfileController(SecurityUtil securityUtil, SysUserService sysUserService) {
        this.securityUtil = securityUtil;
        this.sysUserService = sysUserService;
    }

    @GetMapping
    public ApiResponse<SysUser> me() {
        return ApiResponse.success(securityUtil.currentUser());
    }

    @PutMapping
    public ApiResponse<Void> update(@RequestBody SysUser update) {
        SysUser user = securityUtil.currentUser();
        update.setId(user.getId());
        update.setUsername(user.getUsername());
        update.setPassword(null);
        sysUserService.updateById(update);
        return ApiResponse.success();
    }
}
