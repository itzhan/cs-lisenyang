package com.xiangyongshe.swimadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiangyongshe.swimadmin.entity.SysParam;

public interface SysParamService extends IService<SysParam> {
    String getValue(String key);
}
