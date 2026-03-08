package com.xiangyongshe.swimadmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("course_schedule")
public class CourseSchedule {
    @TableId
    private Long id;
    private Long courseId;
    private Long coachId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate courseDate;
    private String location;
    private Integer maxCapacity;
    private Integer currentCount;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
