package com.xiangyongshe.swimadmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("income_stat")
public class IncomeStat {
    @TableId
    private Long id;
    private LocalDate statDate;
    private Integer orderCount;
    private BigDecimal totalAmount;
    private BigDecimal refundAmount;
    private LocalDateTime createTime;
}
