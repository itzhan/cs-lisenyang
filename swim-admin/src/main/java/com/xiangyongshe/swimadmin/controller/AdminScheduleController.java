package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.dto.ScheduleCoachBindRequest;
import com.xiangyongshe.swimadmin.entity.CourseSchedule;
import com.xiangyongshe.swimadmin.entity.CourseScheduleCoach;
import com.xiangyongshe.swimadmin.service.CourseScheduleCoachService;
import com.xiangyongshe.swimadmin.service.CourseScheduleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/schedules")
public class AdminScheduleController {

    private final CourseScheduleService courseScheduleService;
    private final CourseScheduleCoachService scheduleCoachService;

    public AdminScheduleController(CourseScheduleService courseScheduleService,
                                   CourseScheduleCoachService scheduleCoachService) {
        this.courseScheduleService = courseScheduleService;
        this.scheduleCoachService = scheduleCoachService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<PageResult<CourseSchedule>> list(@RequestParam(defaultValue = "1") long page,
                                                       @RequestParam(defaultValue = "10") long size) {
        QueryWrapper<CourseSchedule> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        Page<CourseSchedule> result = courseScheduleService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<CourseSchedule> create(@RequestBody CourseSchedule schedule) {
        courseScheduleService.save(schedule);
        if (schedule.getCoachId() != null) {
            scheduleCoachService.replaceScheduleCoaches(schedule.getId(), List.of(schedule.getCoachId()));
        }
        return ApiResponse.success(schedule);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody CourseSchedule schedule) {
        schedule.setId(id);
        courseScheduleService.updateById(schedule);
        if (schedule.getCoachId() != null) {
            scheduleCoachService.replaceScheduleCoaches(id, List.of(schedule.getCoachId()));
        }
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        CourseSchedule schedule = courseScheduleService.getById(id);
        if (schedule == null) {
            return ApiResponse.error(404, "排课不存在");
        }
        schedule.setDeleted(1);
        courseScheduleService.updateById(schedule);
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/coaches")
    public ApiResponse<List<CourseScheduleCoach>> coaches(@PathVariable Long id) {
        QueryWrapper<CourseScheduleCoach> wrapper = new QueryWrapper<>();
        wrapper.eq("schedule_id", id).eq("deleted", 0);
        return ApiResponse.success(scheduleCoachService.list(wrapper));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/coaches")
    public ApiResponse<Void> bindCoaches(@PathVariable Long id, @RequestBody ScheduleCoachBindRequest req) {
        scheduleCoachService.replaceScheduleCoaches(id, req == null ? null : req.getCoachIds());
        return ApiResponse.success();
    }
}
