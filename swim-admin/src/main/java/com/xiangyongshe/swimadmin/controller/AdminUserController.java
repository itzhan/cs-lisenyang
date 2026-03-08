package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.dto.AdminUserCreateRequest;
import com.xiangyongshe.swimadmin.dto.AdminUserUpdateRequest;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.service.SysUserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final SysUserService sysUserService;

    public AdminUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<PageResult<SysUser>> list(@RequestParam(defaultValue = "1") long page,
                                                 @RequestParam(defaultValue = "10") long size,
                                                 @RequestParam(required = false) String keyword) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like("username", keyword).or().like("real_name", keyword));
        }
        Page<SysUser> result = sysUserService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<SysUser> create(@Valid @RequestBody AdminUserCreateRequest req) {
        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setRealName(req.getRealName());
        user.setPhone(req.getPhone());
        user.setEmail(req.getEmail());
        user.setStatus(1);
        String roleKey = req.getRoleKey() == null ? "USER" : req.getRoleKey();
        SysUser created = sysUserService.createUserWithRole(user, roleKey);
        return ApiResponse.success(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody AdminUserUpdateRequest req) {
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }
        if (req.getRealName() != null) user.setRealName(req.getRealName());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getStatus() != null) user.setStatus(req.getStatus());
        sysUserService.updateById(user);
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }
        user.setDeleted(1);
        sysUserService.updateById(user);
        return ApiResponse.success();
    }
}
