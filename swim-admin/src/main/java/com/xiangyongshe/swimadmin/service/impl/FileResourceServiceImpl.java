package com.xiangyongshe.swimadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangyongshe.swimadmin.entity.FileResource;
import com.xiangyongshe.swimadmin.mapper.FileResourceMapper;
import com.xiangyongshe.swimadmin.service.FileResourceService;
import org.springframework.stereotype.Service;

@Service
public class FileResourceServiceImpl extends ServiceImpl<FileResourceMapper, FileResource> implements FileResourceService {
}
