package com.xiangyongshe.swimadmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("evaluation")
public class Evaluation {
    @TableId
    private Long id;
    private Long userId;
    private Long courseId;
    private Long coachId;
    private Long reservationId;
    private Integer score;
    private String content;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
