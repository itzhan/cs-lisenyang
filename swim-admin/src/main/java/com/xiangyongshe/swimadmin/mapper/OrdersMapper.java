package com.xiangyongshe.swimadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangyongshe.swimadmin.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    @Select("SELECT COUNT(*) FROM orders WHERE deleted = 0")
    long countAll();

    @Select("SELECT IFNULL(SUM(amount),0) FROM orders WHERE deleted = 0 AND pay_status = 1")
    BigDecimal sumPaidAmount();

    @Select("SELECT IFNULL(SUM(amount),0) FROM orders WHERE deleted = 0 AND pay_status = 2")
    BigDecimal sumRefundAmount();

    @Select("""
            SELECT DATE(create_time) AS day, COUNT(*) AS value
            FROM orders
            WHERE deleted = 0
              AND create_time >= #{start}
              AND create_time < #{end}
            GROUP BY DATE(create_time)
            ORDER BY day
            """)
    List<Map<String, Object>> countByDay(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

    @Select("""
            SELECT pay_status AS status, COUNT(*) AS value
            FROM orders
            WHERE deleted = 0
            GROUP BY pay_status
            """)
    List<Map<String, Object>> countByPayStatus();
}
