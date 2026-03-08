package com.xiangyongshe.swimadmin.controller;

import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.mapper.OrdersMapper;
import com.xiangyongshe.swimadmin.mapper.ReservationMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final OrdersMapper ordersMapper;
    private final ReservationMapper reservationMapper;

    public AdminStatsController(OrdersMapper ordersMapper,
                                ReservationMapper reservationMapper) {
        this.ordersMapper = ordersMapper;
        this.reservationMapper = reservationMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN','COACH')")
    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> overview() {
        long orderCount = ordersMapper.countAll();
        BigDecimal paidAmount = ordersMapper.sumPaidAmount();
        BigDecimal refundAmount = ordersMapper.sumRefundAmount();
        Map<String, Object> data = new HashMap<>();
        data.put("orderCount", orderCount);
        data.put("paidAmount", paidAmount);
        data.put("refundAmount", refundAmount);
        return ApiResponse.success(data);
    }

    @PreAuthorize("hasAnyRole('ADMIN','COACH')")
    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> dashboard() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        List<Map<String, Object>> orderTrendRaw = ordersMapper.countByDay(start, end);
        List<Map<String, Object>> reservationTrendRaw = reservationMapper.countByCreateDay(start, end);

        Map<String, Object> data = new HashMap<>();
        data.put("orderTrend", buildTrend(startDate, today, orderTrendRaw));
        data.put("reservationTrend", buildTrend(startDate, today, reservationTrendRaw));
        data.put("payStatus", ordersMapper.countByPayStatus());
        data.put("reservationStatus", reservationMapper.countByStatus());
        return ApiResponse.success(data);
    }

    private List<Map<String, Object>> buildTrend(LocalDate start,
                                                 LocalDate end,
                                                 List<Map<String, Object>> raw) {
        Map<String, Long> countMap = new HashMap<>();
        if (raw != null) {
            for (Map<String, Object> item : raw) {
                Object dayObj = item.get("day");
                String day;
                if (dayObj instanceof Date date) {
                    day = date.toLocalDate().toString();
                } else {
                    day = String.valueOf(dayObj);
                }
                Number value = (Number) item.getOrDefault("value", 0);
                countMap.put(day, value.longValue());
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate cursor = start;
        while (!cursor.isAfter(end)) {
            String date = cursor.format(formatter);
            long value = countMap.getOrDefault(date, 0L);
            Map<String, Object> row = new HashMap<>();
            row.put("date", date);
            row.put("value", value);
            result.add(row);
            cursor = cursor.plusDays(1);
        }
        return result;
    }
}
