package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.dto.EvaluationCreateRequest;
import com.xiangyongshe.swimadmin.entity.CourseSchedule;
import com.xiangyongshe.swimadmin.entity.Evaluation;
import com.xiangyongshe.swimadmin.entity.Reservation;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.mapper.CourseScheduleMapper;
import com.xiangyongshe.swimadmin.mapper.ReservationMapper;
import com.xiangyongshe.swimadmin.service.EvaluationService;
import com.xiangyongshe.swimadmin.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/evaluations")
public class UserEvaluationController {

    private final EvaluationService evaluationService;
    private final SecurityUtil securityUtil;
    private final ReservationMapper reservationMapper;
    private final CourseScheduleMapper courseScheduleMapper;

    public UserEvaluationController(EvaluationService evaluationService,
                                    SecurityUtil securityUtil,
                                    ReservationMapper reservationMapper,
                                    CourseScheduleMapper courseScheduleMapper) {
        this.evaluationService = evaluationService;
        this.securityUtil = securityUtil;
        this.reservationMapper = reservationMapper;
        this.courseScheduleMapper = courseScheduleMapper;
    }

    @PostMapping
    public ApiResponse<Evaluation> create(@Valid @RequestBody EvaluationCreateRequest req) {
        SysUser user = securityUtil.currentUser();
        Reservation reservation = reservationMapper.selectById(req.getReservationId());
        if (reservation == null || reservation.getDeleted() != null && reservation.getDeleted() == 1) {
            return ApiResponse.error(404, "预约不存在");
        }
        if (!reservation.getUserId().equals(user.getId())) {
            return ApiResponse.error(403, "无权评价该预约");
        }
        CourseSchedule schedule = courseScheduleMapper.selectById(reservation.getScheduleId());
        if (schedule == null || schedule.getDeleted() != null && schedule.getDeleted() == 1) {
            return ApiResponse.error(404, "排课不存在");
        }
        if (!schedule.getCourseId().equals(req.getCourseId())) {
            return ApiResponse.error(400, "课程与预约不匹配");
        }
        Evaluation exists = evaluationService.getOne(new QueryWrapper<Evaluation>()
                .eq("reservation_id", req.getReservationId())
                .eq("user_id", user.getId())
                .eq("deleted", 0));
        if (exists != null) {
            return ApiResponse.error(400, "已评价");
        }
        Evaluation evaluation = new Evaluation();
        evaluation.setUserId(user.getId());
        evaluation.setCourseId(req.getCourseId());
        evaluation.setReservationId(req.getReservationId());
        evaluation.setScore(req.getScore());
        evaluation.setContent(req.getContent());
        evaluation.setStatus(1);
        evaluationService.save(evaluation);
        return ApiResponse.success(evaluation);
    }
}
