package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.entity.Orders;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.service.OrdersService;
import com.xiangyongshe.swimadmin.util.SecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/orders")
public class UserOrderController {

    private final OrdersService ordersService;
    private final SecurityUtil securityUtil;

    public UserOrderController(OrdersService ordersService, SecurityUtil securityUtil) {
        this.ordersService = ordersService;
        this.securityUtil = securityUtil;
    }

    @GetMapping
    public ApiResponse<PageResult<Orders>> list(@RequestParam(defaultValue = "1") long page,
                                                @RequestParam(defaultValue = "10") long size) {
        SysUser user = securityUtil.currentUser();
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId()).eq("deleted", 0);
        Page<Orders> result = ordersService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }
}
