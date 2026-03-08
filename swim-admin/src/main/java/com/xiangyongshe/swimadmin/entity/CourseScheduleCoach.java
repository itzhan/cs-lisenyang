package com.xiangyongshe.swimadmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("course_schedule_coach")
public class CourseScheduleCoach {
    @TableId
    private Long id;
    private Long scheduleId;
    private Long coachId;
    private Integer deleted;
    private LocalDateTime createTime;
}
