package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.dto.ReservationCreateRequest;
import com.xiangyongshe.swimadmin.entity.Reservation;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.service.ReservationService;
import com.xiangyongshe.swimadmin.service.biz.ReservationBizService;
import com.xiangyongshe.swimadmin.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/reservations")
public class UserReservationController {

    private final ReservationService reservationService;
    private final ReservationBizService reservationBizService;
    private final SecurityUtil securityUtil;

    public UserReservationController(ReservationService reservationService,
                                     ReservationBizService reservationBizService,
                                     SecurityUtil securityUtil) {
        this.reservationService = reservationService;
        this.reservationBizService = reservationBizService;
        this.securityUtil = securityUtil;
    }

    @GetMapping
    public ApiResponse<PageResult<Reservation>> list(@RequestParam(defaultValue = "1") long page,
                                                     @RequestParam(defaultValue = "10") long size) {
        SysUser user = securityUtil.currentUser();
        QueryWrapper<Reservation> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId()).eq("deleted", 0);
        Page<Reservation> result = reservationService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @PostMapping
    public ApiResponse<Reservation> create(@Valid @RequestBody ReservationCreateRequest req) {
        SysUser user = securityUtil.currentUser();
        Reservation reservation = reservationBizService.createReservation(user.getId(), req.getScheduleId());
        return ApiResponse.success(reservation);
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id, @RequestParam(required = false) String reason) {
        SysUser user = securityUtil.currentUser();
        reservationBizService.cancelReservation(user.getId(), id, reason);
        return ApiResponse.success();
    }
}
