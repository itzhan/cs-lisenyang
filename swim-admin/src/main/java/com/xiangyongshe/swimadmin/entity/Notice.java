package com.xiangyongshe.swimadmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notice")
public class Notice {
    @TableId
    private Long id;
    private String title;
    private String content;
    private LocalDateTime publishTime;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
