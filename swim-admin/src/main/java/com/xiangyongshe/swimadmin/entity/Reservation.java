package com.xiangyongshe.swimadmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("reservation")
public class Reservation {
    @TableId
    private Long id;
    private Long scheduleId;
    private Long userId;
    private LocalDateTime reserveTime;
    private Integer status;
    private LocalDateTime cancelTime;
    private String cancelReason;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
