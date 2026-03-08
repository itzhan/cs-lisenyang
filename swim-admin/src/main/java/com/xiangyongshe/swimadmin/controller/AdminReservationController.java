package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.entity.Reservation;
import com.xiangyongshe.swimadmin.service.ReservationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<PageResult<Reservation>> list(@RequestParam(defaultValue = "1") long page,
                                                    @RequestParam(defaultValue = "10") long size) {
        QueryWrapper<Reservation> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        Page<Reservation> result = reservationService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Reservation reservation = reservationService.getById(id);
        if (reservation == null) {
            return ApiResponse.error(404, "预约不存在");
        }
        reservation.setStatus(status);
        reservationService.updateById(reservation);
        return ApiResponse.success();
    }
}
