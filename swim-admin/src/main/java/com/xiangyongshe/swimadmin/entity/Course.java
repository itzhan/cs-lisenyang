package com.xiangyongshe.swimadmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {
    @TableId
    private Long id;
    private String courseName;
    private String courseType;
    private String level;
    private Integer durationMin;
    private BigDecimal price;
    private Integer capacity;
    private String coverUrl;
    private String description;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
