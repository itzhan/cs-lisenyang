package com.xiangyongshe.swimadmin.service.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiangyongshe.swimadmin.entity.Course;
import com.xiangyongshe.swimadmin.entity.CourseSchedule;
import com.xiangyongshe.swimadmin.entity.Orders;
import com.xiangyongshe.swimadmin.entity.Reservation;
import com.xiangyongshe.swimadmin.exception.BizException;
import com.xiangyongshe.swimadmin.mapper.CourseMapper;
import com.xiangyongshe.swimadmin.mapper.CourseScheduleMapper;
import com.xiangyongshe.swimadmin.mapper.OrdersMapper;
import com.xiangyongshe.swimadmin.mapper.ReservationMapper;
import com.xiangyongshe.swimadmin.service.SysParamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReservationBizService {

    private final ReservationMapper reservationMapper;
    private final CourseScheduleMapper scheduleMapper;
    private final CourseMapper courseMapper;
    private final OrdersMapper ordersMapper;
    private final SysParamService sysParamService;

    public ReservationBizService(ReservationMapper reservationMapper,
                                 CourseScheduleMapper scheduleMapper,
                                 CourseMapper courseMapper,
                                 OrdersMapper ordersMapper,
                                 SysParamService sysParamService) {
        this.reservationMapper = reservationMapper;
        this.scheduleMapper = scheduleMapper;
        this.courseMapper = courseMapper;
        this.ordersMapper = ordersMapper;
        this.sysParamService = sysParamService;
    }

    @Transactional
    public Reservation createReservation(Long userId, Long scheduleId) {
        CourseSchedule schedule = scheduleMapper.selectById(scheduleId);
        if (schedule == null || schedule.getDeleted() != null && schedule.getDeleted() == 1) {
            throw new BizException("排课不存在");
        }
        if (schedule.getStatus() == null || schedule.getStatus() != 1) {
            throw new BizException("该排课不可预约");
        }
        if (schedule.getStartTime() != null && schedule.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BizException("该排课已开始，无法预约");
        }
        if (schedule.getCurrentCount() >= schedule.getMaxCapacity()) {
            throw new BizException("名额已满");
        }
        int maxPerUser = getIntParam("max_reserve_per_user", 0);
        if (maxPerUser > 0) {
            Long count = reservationMapper.selectCount(new QueryWrapper<Reservation>()
                    .eq("user_id", userId)
                    .eq("deleted", 0)
                    .in("status", 1, 3));
            if (count != null && count >= maxPerUser) {
                throw new BizException("超过最大预约次数限制");
            }
        }
        Reservation exists = reservationMapper.selectOne(new QueryWrapper<Reservation>()
                .eq("schedule_id", scheduleId)
                .eq("user_id", userId)
                .eq("deleted", 0)
                .in("status", 1, 3));
        if (exists != null) {
            throw new BizException("你已预约该课程");
        }

        schedule.setCurrentCount(schedule.getCurrentCount() + 1);
        scheduleMapper.updateById(schedule);

        Reservation reservation = new Reservation();
        reservation.setScheduleId(scheduleId);
        reservation.setUserId(userId);
        reservation.setReserveTime(LocalDateTime.now());
        reservation.setStatus(1);
        reservationMapper.insert(reservation);

        Course course = courseMapper.selectById(schedule.getCourseId());
        BigDecimal amount = course != null ? course.getPrice() : BigDecimal.ZERO;

        Orders order = new Orders();
        order.setOrderNo("ORD" + UUID.randomUUID().toString().replace("-", ""));
        order.setUserId(userId);
        order.setReservationId(reservation.getId());
        order.setAmount(amount);
        order.setPayStatus(0);
        ordersMapper.insert(order);

        return reservation;
    }

    @Transactional
    public void cancelReservation(Long userId, Long reservationId, String reason) {
        Reservation reservation = reservationMapper.selectById(reservationId);
        if (reservation == null || reservation.getDeleted() != null && reservation.getDeleted() == 1) {
            throw new BizException("预约不存在");
        }
        if (!reservation.getUserId().equals(userId)) {
            throw new BizException("无权取消此预约");
        }
        if (reservation.getStatus() != null && reservation.getStatus() != 1) {
            throw new BizException("预约状态不可取消");
        }
        CourseSchedule schedule = scheduleMapper.selectById(reservation.getScheduleId());
        if (schedule == null || schedule.getDeleted() != null && schedule.getDeleted() == 1) {
            throw new BizException("排课不存在");
        }
        int cancelBeforeMinutes = getIntParam("cancel_before_minutes", 0);
        if (cancelBeforeMinutes > 0 && schedule.getStartTime() != null) {
            LocalDateTime latestCancelTime = schedule.getStartTime().minusMinutes(cancelBeforeMinutes);
            if (LocalDateTime.now().isAfter(latestCancelTime)) {
                throw new BizException("已过可取消时间");
            }
        }
        reservation.setStatus(2);
        reservation.setCancelTime(LocalDateTime.now());
        reservation.setCancelReason(reason);
        reservationMapper.updateById(reservation);

        if (schedule != null && schedule.getCurrentCount() != null && schedule.getCurrentCount() > 0) {
            schedule.setCurrentCount(schedule.getCurrentCount() - 1);
            scheduleMapper.updateById(schedule);
        }

        if (isRefundEnabled()) {
            Orders order = ordersMapper.selectOne(new QueryWrapper<Orders>()
                    .eq("reservation_id", reservationId)
                    .eq("deleted", 0));
            if (order != null && order.getPayStatus() != null && order.getPayStatus() == 1) {
                order.setPayStatus(2);
                order.setRefundStatus(1);
                order.setRefundTime(LocalDateTime.now());
                ordersMapper.updateById(order);
            }
        }
    }

    private int getIntParam(String key, int defaultValue) {
        String value = sysParamService.getValue(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private boolean isRefundEnabled() {
        return getIntParam("refund_enabled", 0) == 1;
    }
}
