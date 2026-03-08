package com.xiangyongshe.swimadmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("coach_profile")
public class CoachProfile {
    @TableId
    private Long id;
    private Long userId;
    private String qualification;
    private String specialty;
    private String intro;
    private String avatarUrl;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
