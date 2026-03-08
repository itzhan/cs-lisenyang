package com.xiangyongshe.swimadmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("file_resource")
public class FileResource {
    @TableId
    private Long id;
    private String bizType;
    private Long bizId;
    private String fileName;
    private String filePath;
    private String fileUrl;
    private String contentType;
    private Long fileSize;
    private Long uploaderId;
    private Integer deleted;
    private LocalDateTime createTime;
}
