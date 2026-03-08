package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.entity.CoachProfile;
import com.xiangyongshe.swimadmin.service.CoachProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/coaches")
public class AdminCoachController {

    private final CoachProfileService coachProfileService;

    public AdminCoachController(CoachProfileService coachProfileService) {
        this.coachProfileService = coachProfileService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<PageResult<CoachProfile>> list(@RequestParam(defaultValue = "1") long page,
                                                     @RequestParam(defaultValue = "10") long size,
                                                     @RequestParam(required = false) String keyword) {
        QueryWrapper<CoachProfile> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like("qualification", keyword)
                    .or().like("specialty", keyword)
                    .or().like("intro", keyword));
        }
        Page<CoachProfile> result = coachProfileService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<CoachProfile> create(@RequestBody CoachProfile coachProfile) {
        if (coachProfile.getUserId() == null) {
            return ApiResponse.error(400, "教练账号不能为空");
        }
        if (coachProfile.getStatus() == null) {
            coachProfile.setStatus(1);
        }
        coachProfileService.save(coachProfile);
        return ApiResponse.success(coachProfile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody CoachProfile coachProfile) {
        coachProfile.setId(id);
        coachProfileService.updateById(coachProfile);
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        CoachProfile profile = coachProfileService.getById(id);
        if (profile == null) {
            return ApiResponse.error(404, "教练不存在");
        }
        profile.setDeleted(1);
        coachProfileService.updateById(profile);
        return ApiResponse.success();
    }
}
