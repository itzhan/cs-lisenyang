package com.xiangyongshe.swimadmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Orders {
    @TableId
    private Long id;
    private String orderNo;
    private Long userId;
    private Long reservationId;
    private BigDecimal amount;
    private Integer payStatus;
    private Integer payType;
    private LocalDateTime payTime;
    private Integer refundStatus;
    private LocalDateTime refundTime;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
