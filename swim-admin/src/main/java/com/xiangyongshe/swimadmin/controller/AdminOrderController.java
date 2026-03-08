package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.entity.Orders;
import com.xiangyongshe.swimadmin.service.OrdersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrdersService ordersService;

    public AdminOrderController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<PageResult<Orders>> list(@RequestParam(defaultValue = "1") long page,
                                               @RequestParam(defaultValue = "10") long size) {
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        Page<Orders> result = ordersService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/pay-status")
    public ApiResponse<Void> updatePayStatus(@PathVariable Long id, @RequestParam Integer payStatus) {
        Orders order = ordersService.getById(id);
        if (order == null) {
            return ApiResponse.error(404, "订单不存在");
        }
        order.setPayStatus(payStatus);
        ordersService.updateById(order);
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/refund")
    public ApiResponse<Void> refund(@PathVariable Long id) {
        Orders order = ordersService.getById(id);
        if (order == null) {
            return ApiResponse.error(404, "订单不存在");
        }
        if (order.getPayStatus() == null || order.getPayStatus() != 1) {
            return ApiResponse.error(400, "订单未支付，无法退款");
        }
        order.setPayStatus(2);
        order.setRefundStatus(1);
        order.setRefundTime(java.time.LocalDateTime.now());
        ordersService.updateById(order);
        return ApiResponse.success();
    }
}
