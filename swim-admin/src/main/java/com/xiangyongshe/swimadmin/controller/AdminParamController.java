package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.entity.SysParam;
import com.xiangyongshe.swimadmin.service.SysParamService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/params")
public class AdminParamController {

    private final SysParamService sysParamService;

    public AdminParamController(SysParamService sysParamService) {
        this.sysParamService = sysParamService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<PageResult<SysParam>> list(@RequestParam(defaultValue = "1") long page,
                                                 @RequestParam(defaultValue = "10") long size) {
        Page<SysParam> result = sysParamService.page(new Page<>(page, size));
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<SysParam> create(@RequestBody SysParam param) {
        sysParamService.save(param);
        return ApiResponse.success(param);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody SysParam param) {
        param.setId(id);
        sysParamService.updateById(param);
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        sysParamService.removeById(id);
        return ApiResponse.success();
    }
}
