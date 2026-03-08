package com.xiangyongshe.swimadmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("feedback")
public class Feedback {
    @TableId
    private Long id;
    private Long userId;
    private String type;
    private String content;
    private Integer status;
    private String replyContent;
    private LocalDateTime replyTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
