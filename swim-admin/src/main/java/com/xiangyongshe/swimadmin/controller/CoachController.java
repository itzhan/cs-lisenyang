package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.entity.CoachProfile;
import com.xiangyongshe.swimadmin.entity.CourseSchedule;
import com.xiangyongshe.swimadmin.entity.CourseScheduleCoach;
import com.xiangyongshe.swimadmin.entity.Reservation;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.exception.BizException;
import com.xiangyongshe.swimadmin.service.CoachProfileService;
import com.xiangyongshe.swimadmin.service.CourseScheduleCoachService;
import com.xiangyongshe.swimadmin.service.CourseScheduleService;
import com.xiangyongshe.swimadmin.service.ReservationService;
import com.xiangyongshe.swimadmin.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/coach")
public class CoachController {

    private final SecurityUtil securityUtil;
    private final CoachProfileService coachProfileService;
    private final CourseScheduleCoachService scheduleCoachService;
    private final CourseScheduleService courseScheduleService;
    private final ReservationService reservationService;

    public CoachController(SecurityUtil securityUtil,
                           CoachProfileService coachProfileService,
                           CourseScheduleCoachService scheduleCoachService,
                           CourseScheduleService courseScheduleService,
                           ReservationService reservationService) {
        this.securityUtil = securityUtil;
        this.coachProfileService = coachProfileService;
        this.scheduleCoachService = scheduleCoachService;
        this.courseScheduleService = courseScheduleService;
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasRole('COACH')")
    @GetMapping("/schedules")
    public ApiResponse<PageResult<CourseSchedule>> schedules(@RequestParam(defaultValue = "1") long page,
                                                             @RequestParam(defaultValue = "10") long size) {
        Long coachId = getCoachProfileId();
        QueryWrapper<CourseSchedule> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        List<Long> scheduleIds = scheduleCoachService.list(new QueryWrapper<CourseScheduleCoach>()
                        .eq("coach_id", coachId)
                        .eq("deleted", 0))
                .stream().map(CourseScheduleCoach::getScheduleId).toList();
        if (!scheduleIds.isEmpty()) {
            wrapper.and(w -> w.in("id", scheduleIds).or().eq("coach_id", coachId));
        } else {
            wrapper.eq("coach_id", coachId);
        }
        Page<CourseSchedule> result = courseScheduleService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @PreAuthorize("hasRole('COACH')")
    @GetMapping("/reservations")
    public ApiResponse<PageResult<Reservation>> reservations(@RequestParam(defaultValue = "1") long page,
                                                             @RequestParam(defaultValue = "10") long size) {
        Long coachId = getCoachProfileId();
        List<Long> scheduleIds = scheduleCoachService.list(new QueryWrapper<CourseScheduleCoach>()
                        .eq("coach_id", coachId)
                        .eq("deleted", 0))
                .stream().map(CourseScheduleCoach::getScheduleId).toList();
        List<Long> mainScheduleIds = courseScheduleService.list(new QueryWrapper<CourseSchedule>()
                        .eq("coach_id", coachId)
                        .eq("deleted", 0))
                .stream().map(CourseSchedule::getId).toList();
        if (!mainScheduleIds.isEmpty()) {
            scheduleIds = new java.util.ArrayList<>(scheduleIds);
            scheduleIds.addAll(mainScheduleIds);
            scheduleIds = scheduleIds.stream().distinct().toList();
        }
        QueryWrapper<Reservation> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        if (!scheduleIds.isEmpty()) {
            wrapper.in("schedule_id", scheduleIds);
        } else {
            wrapper.eq("schedule_id", -1);
        }
        Page<Reservation> result = reservationService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @PreAuthorize("hasRole('COACH')")
    @PutMapping("/reservations/{id}/status")
    public ApiResponse<Void> updateReservationStatus(@PathVariable Long id, @RequestParam Integer status) {
        Long coachId = getCoachProfileId();
        Reservation reservation = reservationService.getById(id);
        if (reservation == null || reservation.getDeleted() != null && reservation.getDeleted() == 1) {
            return ApiResponse.error(404, "预约不存在");
        }
        CourseSchedule schedule = courseScheduleService.getById(reservation.getScheduleId());
        if (schedule == null || schedule.getDeleted() != null && schedule.getDeleted() == 1) {
            return ApiResponse.error(404, "排课不存在");
        }
        boolean allowed = scheduleCoachService.count(new QueryWrapper<CourseScheduleCoach>()
                .eq("schedule_id", schedule.getId())
                .eq("coach_id", coachId)
                .eq("deleted", 0)) > 0 || (schedule.getCoachId() != null && schedule.getCoachId().equals(coachId));
        if (!allowed) {
            return ApiResponse.error(403, "无权操作该预约");
        }
        reservation.setStatus(status);
        if (status != null && status == 3) {
            reservation.setCancelTime(null);
        }
        reservation.setUpdateTime(LocalDateTime.now());
        reservationService.updateById(reservation);
        return ApiResponse.success();
    }

    private Long getCoachProfileId() {
        SysUser user = securityUtil.currentUser();
        if (user == null) {
            throw new BizException("未登录");
        }
        CoachProfile profile = coachProfileService.getOne(new QueryWrapper<CoachProfile>()
                .eq("user_id", user.getId())
                .eq("deleted", 0));
        if (profile == null) {
            throw new BizException("未绑定教练档案");
        }
        return profile.getId();
    }
}
