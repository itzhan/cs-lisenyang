package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.entity.CoachProfile;
import com.xiangyongshe.swimadmin.entity.Course;
import com.xiangyongshe.swimadmin.entity.CourseSchedule;
import com.xiangyongshe.swimadmin.entity.Notice;
import com.xiangyongshe.swimadmin.service.CoachProfileService;
import com.xiangyongshe.swimadmin.service.CourseScheduleService;
import com.xiangyongshe.swimadmin.service.CourseService;
import com.xiangyongshe.swimadmin.service.NoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final CourseService courseService;
    private final CoachProfileService coachProfileService;
    private final CourseScheduleService courseScheduleService;
    private final NoticeService noticeService;

    public PublicController(CourseService courseService,
                            CoachProfileService coachProfileService,
                            CourseScheduleService courseScheduleService,
                            NoticeService noticeService) {
        this.courseService = courseService;
        this.coachProfileService = coachProfileService;
        this.courseScheduleService = courseScheduleService;
        this.noticeService = noticeService;
    }

    @GetMapping("/courses")
    public ApiResponse<PageResult<Course>> courses(@RequestParam(defaultValue = "1") long page,
                                                   @RequestParam(defaultValue = "10") long size) {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0).eq("status", 1);
        Page<Course> result = courseService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/coaches")
    public ApiResponse<PageResult<CoachProfile>> coaches(@RequestParam(defaultValue = "1") long page,
                                                         @RequestParam(defaultValue = "10") long size) {
        QueryWrapper<CoachProfile> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0).eq("status", 1);
        Page<CoachProfile> result = coachProfileService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/schedules")
    public ApiResponse<PageResult<CourseSchedule>> schedules(@RequestParam(defaultValue = "1") long page,
                                                             @RequestParam(defaultValue = "10") long size) {
        QueryWrapper<CourseSchedule> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0).eq("status", 1);
        Page<CourseSchedule> result = courseScheduleService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/notices")
    public ApiResponse<PageResult<Notice>> notices(@RequestParam(defaultValue = "1") long page,
                                                  @RequestParam(defaultValue = "10") long size) {
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        Page<Notice> result = noticeService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }
}
