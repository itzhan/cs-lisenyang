package com.xiangyongshe.swimadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangyongshe.swimadmin.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {
    @Select("""
            SELECT DATE(create_time) AS day, COUNT(*) AS value
            FROM reservation
            WHERE deleted = 0
              AND create_time >= #{start}
              AND create_time < #{end}
            GROUP BY DATE(create_time)
            ORDER BY day
            """)
    List<Map<String, Object>> countByCreateDay(@Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);

    @Select("""
            SELECT status AS status, COUNT(*) AS value
            FROM reservation
            WHERE deleted = 0
            GROUP BY status
            """)
    List<Map<String, Object>> countByStatus();
}
